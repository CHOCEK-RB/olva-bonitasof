package com.olva.enviosapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManifiestoInputDTO {

    @NotBlank(message = "El código del manifiesto es obligatorio")
    private String codigoManifiesto;

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    @NotNull(message = "La cantidad de paquetes es obligatoria")
    @Min(value = 1, message = "La cantidad de paquetes debe ser mayor a 0")
    private Integer cantidadPaquetes;
}
