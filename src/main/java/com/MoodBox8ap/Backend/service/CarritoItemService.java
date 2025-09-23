package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.CarritoItem;
import com.MoodBox8ap.Backend.model.Producto;
import com.MoodBox8ap.Backend.model.Usuario;
import com.MoodBox8ap.Backend.repository.ICarritoItemRepository;
import com.MoodBox8ap.Backend.repository.IProductoRepository;
import com.MoodBox8ap.Backend.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoItemService implements ICarritoItemService {

    private final ICarritoItemRepository carritoItemRepository;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;

    public CarritoItemService(ICarritoItemRepository carritoItemRepository,
                              IProductoRepository productoRepository,
                              IUsuarioRepository usuarioRepository) {
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<CarritoItem> obtenerCarritoPorUsuario(Long usuarioId) {
        return carritoItemRepository.findByUsuario_IdUsuario(usuarioId);
    }

    @Override
    public CarritoItem agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoItem existente = carritoItemRepository
                .findByUsuario_IdUsuarioAndProducto_IdProducto(usuarioId, productoId);

        if (existente != null) {
            /**
             *  Lógica clave:
             * - Si la cantidad recibida es 1 → viene del catálogo → SUMAR
             * - Si la cantidad recibida es > 1 → viene del carrito → REEMPLAZAR
             */
            if (cantidad == 1) {
                int nuevaCantidad = existente.getCantidad() + 1;

                if (nuevaCantidad > producto.getStock()) {
                    throw new IllegalArgumentException("Stock insuficiente");
                }

                existente.setCantidad(nuevaCantidad);
            } else {
                // Acción desde el carrito → reemplazar
                if (cantidad > producto.getStock()) {
                    throw new IllegalArgumentException("Stock insuficiente");
                }
                existente.setCantidad(cantidad);
            }

            return carritoItemRepository.save(existente);
        } else {
            if (cantidad > producto.getStock()) {
                throw new IllegalArgumentException("Stock insuficiente");
            }

            CarritoItem nuevo = new CarritoItem();
            nuevo.setUsuario(usuario);
            nuevo.setProducto(producto);
            nuevo.setCantidad(cantidad);
            return carritoItemRepository.save(nuevo);
        }
    }



    @Override
    public void eliminarItem(Long itemId) {
        carritoItemRepository.deleteById(itemId);
    }


    @Override
    public void vaciarCarrito(Long usuarioId) {
        carritoItemRepository.deleteByUsuario_IdUsuario(usuarioId);
    }
}
