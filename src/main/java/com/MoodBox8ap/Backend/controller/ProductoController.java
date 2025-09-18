package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.model.Producto;
import com.MoodBox8ap.Backend.service.IProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:8080"})
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto guardarProducto(@RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.obtenerPorId(id)
                .map(p -> {
                    // Actualizar solo los campos editables
                    p.setNombre(producto.getNombre());
                    p.setDescripcion(producto.getDescripcion());
                    p.setPrecio(producto.getPrecio());
                    p.setStock(producto.getStock());
                    p.setCodigo(producto.getCodigo());
                    p.setCategoria(producto.getCategoria());
                    p.setImagen(producto.getImagen());

                    // Mantener el estado actual si no viene en el JSON
                    if (producto.getEstado() != null) {
                        p.setEstado(producto.getEstado());
                    }

                    return ResponseEntity.ok(productoService.guardarProducto(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Producto> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        System.out.println("📥 Body recibido: " + body);

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null ||
                (!nuevoEstado.equals("activo") && !nuevoEstado.equals("inactivo"))) {
            return ResponseEntity.badRequest().build();
        }

        return productoService.obtenerPorId(id)
                .map(producto -> {
                    producto.setEstado(
                            nuevoEstado.equals("activo")
                                    ? Producto.Estado.activo
                                    : Producto.Estado.inactivo
                    );
                    return ResponseEntity.ok(productoService.guardarProducto(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id ) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
