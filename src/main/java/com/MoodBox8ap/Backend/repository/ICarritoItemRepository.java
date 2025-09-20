package com.MoodBox8ap.Backend.repository;

import com.MoodBox8ap.Backend.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuario_IdUsuario(Long usuarioId);
    CarritoItem findByUsuario_IdUsuarioAndProducto_IdProducto(Long usuarioId, Long productoId);
    void deleteByUsuario_IdUsuario(Long usuarioId);
}
