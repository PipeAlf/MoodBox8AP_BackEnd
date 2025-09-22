package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.model.CarritoItem;
import com.MoodBox8ap.Backend.service.ICarritoItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.MoodBox8ap.Backend.dto.CarritoItemRequest;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:8080", "https://main.drkoft4my5rgd.amplifyapp.com"})
public class CarritoController {

    private final ICarritoItemService carritoService;

    public CarritoController(ICarritoItemService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/{usuarioId}")
    public List<CarritoItem> obtenerCarrito(@PathVariable Long usuarioId) {
        return carritoService.obtenerCarritoPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<CarritoItem> agregarProducto(@RequestBody CarritoItemRequest req) {
        CarritoItem nuevo = carritoService.agregarProducto(req.getUsuarioId(), req.getProductoId(), req.getCantidad());
        return ResponseEntity.ok(nuevo);
    }


    @DeleteMapping("/{itemId}")
    public void eliminarItem(@PathVariable Long itemId) {
        carritoService.eliminarItem(itemId);
    }

    @DeleteMapping("/vaciar/{usuarioId}")
    public void vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
    }
}

