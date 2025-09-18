package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.DetalleVenta;
import com.MoodBox8ap.Backend.repository.IDetalleVentaRepository;
import com.MoodBox8ap.Backend.service.IDetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService implements IDetalleVentaService {

    private final IDetalleVentaRepository detalleVentaRepository;

    @Autowired
    public DetalleVentaService(IDetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public List<DetalleVenta> listarDetalles() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public Optional<DetalleVenta> obtenerPorId(Long id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    public DetalleVenta guardarDetalle(DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminarDetalle(Long id) {
        detalleVentaRepository.deleteById(id);
    }
}
