package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.Venta;
import java.util.List;
import java.util.Optional;

public interface IVentaService {
    List<Venta> listarVentas();
    Optional<Venta> obtenerPorId(Long id);
    Venta guardarVenta(Venta venta);
    void eliminarVenta(Long id);
}
