package com.amaan.backend.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineRequest {

    @NotNull
    private UUID borrowRecordId;
}