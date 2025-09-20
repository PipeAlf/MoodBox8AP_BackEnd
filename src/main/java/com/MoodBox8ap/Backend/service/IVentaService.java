package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.Venta;

public interface IVentaService {
    Venta realizarVenta(Long usuarioId, String metodoPago);
}
