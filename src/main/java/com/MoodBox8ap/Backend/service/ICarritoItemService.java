package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.CarritoItem;

import java.util.List;

public interface ICarritoItemService {
    List<CarritoItem> obtenerCarritoPorUsuario(Long usuarioId);
    CarritoItem agregarProducto(Long usuarioId, Long productoId, int cantidad);
    void eliminarItem(Long itemId);
    void vaciarCarrito(Long usuarioId);
}

