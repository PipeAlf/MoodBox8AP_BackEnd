package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.model.Producto;
import com.MoodBox8ap.Backend.service.IProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
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

    @GetMapping("/activos")
    public ResponseEntity<List<Map<String, Object>>> obtenerProductosActivos() {
        List<Producto> productos = productoService.listarProductosActivos();

        // Convertimos cada producto a un Map con "categorias" como array
        List<Map<String, Object>> productosConCategorias = productos.stream().map(producto -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", producto.getIdProducto());
            map.put("nombre", producto.getNombre());
            map.put("descripcion", producto.getDescripcion());
            map.put("precio", producto.getPrecio());
            map.put("stock", producto.getStock());
            map.put("codigo", producto.getCodigo());
            map.put("imagen", producto.getImagen());
            map.put("estado", producto.getEstado());

            // convertir la cadena "categoria" en array
            List<String> categorias = Arrays.stream(producto.getCategoria().split(","))
                    .map(String::trim)
                    .filter(c -> !c.isBlank())
                    .toList();
            map.put("categorias", categorias);

            return map;
        }).toList();

        return ResponseEntity.ok(productosConCategorias);
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

        System.out.println(" Body recibido: " + body);

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
