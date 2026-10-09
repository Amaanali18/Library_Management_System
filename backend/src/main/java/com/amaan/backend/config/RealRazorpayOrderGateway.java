package com.amaan.backend.config;

import com.amaan.backend.services.RazorpayOrderGateway;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConditionalOnProperty(
        name = "app.payment.razorpay.mock",
        havingValue = "false",
        matchIfMissing = true
)
public class RealRazorpayOrderGateway implements RazorpayOrderGateway {

    private final RazorpayClient razorpayClient;

    public RealRazorpayOrderGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String createOrder(
            BigDecimal amount,
            String currency,
            String receipt
    ) throws Exception {

        long amountInSmallestUnit =
                amount.movePointRight(2).longValueExact();

        JSONObject request = new JSONObject();
        request.put("amount", amountInSmallestUnit);
        request.put("currency", currency);
        request.put("receipt", receipt);

        Order order = razorpayClient.orders.create(request);

        return order.get("id");
    }
}