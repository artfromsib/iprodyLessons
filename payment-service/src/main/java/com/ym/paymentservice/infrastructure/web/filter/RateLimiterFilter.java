package com.ym.paymentservice.infrastructure.web.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimiterFilter implements Filter {

    private final ConcurrentHashMap<String, Bucket> BUCKETS = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String remoteAddr = request.getRemoteAddr();
        Bucket bucket = BUCKETS.computeIfAbsent(remoteAddr, this::newBucket);
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too Many Requests");
        }
    }

    private Bucket newBucket(String s) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(50)
                .refillIntervally(1, Duration.ofSeconds(5))
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
