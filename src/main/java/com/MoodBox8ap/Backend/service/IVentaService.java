package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.Venta;

import java.util.List;

public interface IVentaService {
    Venta realizarVenta(Long usuarioId, String metodoPago);
    List<Venta> obtenerTodasLasVentas();
    Venta obtenerVentaPorId(Long id);
}
