package com.MoodBox8ap.Backend.repository;

import com.MoodBox8ap.Backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(Producto.Estado estado);
}
