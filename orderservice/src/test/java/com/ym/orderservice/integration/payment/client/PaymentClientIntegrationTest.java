package com.ym.orderservice.integration.payment.client;

import com.ym.orderservice.integration.payment.dto.request.PayRequestDTO;
import com.ym.orderservice.integration.payment.dto.response.PayResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@EnableWireMock (
        @ConfigureWireMock (
        name = "payment-service-mock",
        port = 9999,
                baseUrlProperties = "http://localhost",
                filesUnderClasspath = "wiremock"
        )
)
public class PaymentClientIntegrationTest {
    @Autowired
    private  PaymentClient paymentClient;

    @Test
    void testPayOrder() {
        PayRequestDTO  request = new PayRequestDTO(12345L,
                UUID.randomUUID(),
                new BigDecimal("99.99"),
                Currency.getInstance("USD") );
        PayResponseDTO response = paymentClient.payOrder(request);
        assertTrue(response.paid());
    }
}
