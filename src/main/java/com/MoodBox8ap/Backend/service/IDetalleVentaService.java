package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    List<DetalleVenta> listarDetalles();
    Optional<DetalleVenta> obtenerPorId(Long id);
    DetalleVenta guardarDetalle(DetalleVenta detalleVenta);
    void eliminarDetalle(Long id);
}
