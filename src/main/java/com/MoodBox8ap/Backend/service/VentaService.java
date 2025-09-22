package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.*;
import com.MoodBox8ap.Backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements IVentaService {

    private final IVentaRepository ventaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ICarritoItemRepository carritoItemRepository;
    private final IProductoRepository productoRepository;

    public VentaService(IVentaRepository ventaRepository,
                        IUsuarioRepository usuarioRepository,
                        ICarritoItemRepository carritoItemRepository,
                        IProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public Venta realizarVenta(Long usuarioId, String metodoPagoStr) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<CarritoItem> carrito = carritoItemRepository.findByUsuario_IdUsuario(usuarioId);
        if (carrito.isEmpty()) throw new IllegalStateException("El carrito está vacío");

        Venta.MetodoPago metodoPago = Venta.MetodoPago.valueOf(metodoPagoStr.toLowerCase());

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setMetodoPago(metodoPago);
        venta.setEstado(Venta.Estado.pagada); // o pendiente si necesitas validar pago
        venta.setDetalles(new ArrayList<>());

        double total = 0.0;

        for (CarritoItem item : carrito) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Restar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * item.getCantidad());

            venta.getDetalles().add(detalle);
            total += detalle.getSubtotal();
        }


        venta.setTotal(total);

        // Guardar la venta (con cascade guarda los detalles)
        Venta guardada = ventaRepository.save(venta);

        // Vaciar carrito
        carritoItemRepository.deleteByUsuario_IdUsuario(usuarioId);

        return guardada;
    }
    @Override
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
    }

}
