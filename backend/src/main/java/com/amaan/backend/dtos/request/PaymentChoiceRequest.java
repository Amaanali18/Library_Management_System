
package com.amaan.backend.dtos.request;

import jakarta.validation.constraints.NotNull;

public record PaymentChoiceRequest(
        @NotNull PaymentChoice choice
) {
    public enum PaymentChoice {
        PAY_NOW,
        PAY_LATER
    }
}
