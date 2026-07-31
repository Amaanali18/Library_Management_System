package com.amaan.backend.helpers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {
    @NotBlank
    @Size(min=3, max=100)
    private String name;
    @NotBlank
    @Email
    @Size(max=254)
    private String email;
    @NotBlank
    @Size(min=8,max=72)
    private String password;
}
