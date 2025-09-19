package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarProductos();
    Optional<Producto> obtenerPorId(Long id);
    Producto guardarProducto(Producto producto);
    void eliminarProducto(Long id);
    List<Producto> listarProductosActivos();
}
