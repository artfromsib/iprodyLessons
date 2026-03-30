package com.ym.paymentservice.infrastructure.web.interceptor;

import com.ym.paymentservice.application.service.IdempotencyService;
import com.ym.paymentservice.domain.model.enums.KeyStatus;
import com.ym.paymentservice.infrastructure.persistence.entity.IdempotencyKey;
import com.ym.paymentservice.interfaces.exception.IdempotencyKeyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static com.ym.paymentservice.constant.WebConstants.WRAPPED_RESPONSE_ATTRIBUTE_NAME;

@RequiredArgsConstructor
@Component
public class IdempotencyInterceptor implements HandlerInterceptor {
    private static final String KEY_NAME = "X-Idempotency-key";
    private final IdempotencyService idempotencyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpMethod metod = HttpMethod.valueOf(request.getMethod());

        if (metod.equals(HttpMethod.POST)) {
            String idempotencyKey = request.getHeader(KEY_NAME);

            if (idempotencyKey.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().println("X-Idempotency-key header is not present");
                return false;
            }
            return processIdempotency(idempotencyKey, response);
        }
        return true;
    }

    private boolean processIdempotency(String idempotencyKey, HttpServletResponse response) throws IOException {
        Optional<IdempotencyKey> existingKey = idempotencyService.getByKey(idempotencyKey);
        if (existingKey.isPresent()) {
            return processExistingKey(existingKey.get(), response);
        } else {
            return createNewKey(idempotencyKey, response);
        }
    }

    private boolean processExistingKey(IdempotencyKey idempotencyKey, HttpServletResponse response) throws IOException {
        KeyStatus status = idempotencyKey.getStatus();
        if (status == KeyStatus.PENDING) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.getWriter().println("Same request is already in progress...");
        } else if (status == KeyStatus.COMPLETED) {
            response.setStatus(idempotencyKey.getStatusCode());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(idempotencyKey.getResponse());
        } else {
            throw new IllegalArgumentException("Invalid status of idempotency key");
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        if (method.equals(HttpMethod.POST)) {
            ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) request.getAttribute(WRAPPED_RESPONSE_ATTRIBUTE_NAME);
            String responseBody = new String(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding());

            var idempotencyKey = request.getHeader(KEY_NAME);
            idempotencyService.markKeyAsCompleted(idempotencyKey, responseBody, response.getStatus());
        }
    }

    private boolean createNewKey(String idempotencyKey, HttpServletResponse response) throws IOException {
        try {
            idempotencyService.createPendingKey(idempotencyKey);
            return true;
        } catch (IdempotencyKeyExistsException e) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.getWriter().println("Same request is already in progress...");
            return false;
        }
    }
}
