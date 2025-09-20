package com.MoodBox8ap.Backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VentaRequest {
    private Long usuarioId;
    private String metodoPago; // "tarjeta", "efectivo", "transferencia"
}
