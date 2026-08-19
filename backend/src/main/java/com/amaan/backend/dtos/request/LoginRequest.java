package com.amaan.backend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank
    @Size(max=254)
    private String username;
    @NotBlank
    @Size(min=8,max=72)
    private String password;
}
