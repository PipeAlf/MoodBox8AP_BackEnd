package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.dto.VentaRequest;
import com.MoodBox8ap.Backend.model.Venta;
import com.MoodBox8ap.Backend.service.IVentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "https://main.drkoft4my5rgd.amplifyapp.com"})
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<Venta> realizarCompra(@RequestBody VentaRequest request) {
        Venta venta = ventaService.realizarVenta(request.getUsuarioId(), request.getMetodoPago());
        return ResponseEntity.ok(venta);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Venta> obtenerTodasLasVentas() {
        return ventaService.obtenerTodasLasVentas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        Venta venta = ventaService.obtenerVentaPorId(id);
        return ResponseEntity.ok(venta);
    }


}
