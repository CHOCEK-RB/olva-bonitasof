package com.olva.enviosapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de solicitud para clasificar un envío en el almacén mediante su número de tracking.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClasificarRequest {
  private String numeroTracking;
}
