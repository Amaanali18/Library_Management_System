package com.amaan.backend.config;

import com.razorpay.RazorpayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        name = "app.payment.razorpay.mock",
        havingValue = "false",
        matchIfMissing = true
)
public class RazorpayConfig {

    @Bean
    public RazorpayClient razorpayClient(
            @Value("${app.payment.razorpay.key-id}") String keyId,
            @Value("${app.payment.razorpay.key-secret}") String keySecret
    ) throws Exception {
        return new RazorpayClient(keyId, keySecret);
    }
}