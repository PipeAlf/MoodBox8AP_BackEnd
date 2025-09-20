package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.dto.VentaRequest;
import com.MoodBox8ap.Backend.model.Venta;
import com.MoodBox8ap.Backend.service.IVentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
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
}
