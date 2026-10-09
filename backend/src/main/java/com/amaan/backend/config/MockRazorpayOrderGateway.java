package com.amaan.backend.config;

import com.amaan.backend.services.RazorpayOrderGateway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@ConditionalOnProperty(
        name = "app.payment.razorpay.mock",
        havingValue = "true"
)
public class MockRazorpayOrderGateway implements RazorpayOrderGateway {

    @Override
    public String createOrder(
            BigDecimal amount,
            String currency,
            String receipt
    ) {
        String orderId = "order_mock_" +
                UUID.randomUUID().toString().replace("-", "");

        System.out.println(
                "[MOCK RAZORPAY] Order created: " +
                        orderId +
                        " | Amount: " + amount +
                        " " + currency +
                        " | Receipt: " + receipt
        );

        return orderId;
    }
}