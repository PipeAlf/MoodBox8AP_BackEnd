package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.model.DetalleVenta;
import com.MoodBox8ap.Backend.service.IDetalleVentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles")
@CrossOrigin(origins = "https://main.drkoft4my5rgd.amplifyapp.com")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    public List<DetalleVenta> listarDetalles() {
        return detalleVentaService.listarDetalles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetalle(@PathVariable Long id) {
        return detalleVentaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DetalleVenta guardarDetalle(@RequestBody DetalleVenta detalleVenta) {
        return detalleVentaService.guardarDetalle(detalleVenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalle(@PathVariable Long id, @RequestBody DetalleVenta detalleVenta) {
        return detalleVentaService.obtenerPorId(id)
                .map(d -> {
                    detalleVenta.setIdDetalle(id);
                    return ResponseEntity.ok(detalleVentaService.guardarDetalle(detalleVenta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long id ) {
        detalleVentaService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }
}
