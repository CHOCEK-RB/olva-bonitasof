package com.olva.enviosapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmacionCargaDTO {

  // Variable para la tarea: Cargar mercancía (Paso 1)
  private Boolean confirmarCargaInput;

  // Variable para la tarea: Registrar recepción (Paso 2)
  private Boolean confirmarRecepcionInput;
}
