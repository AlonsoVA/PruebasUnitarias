package com.example.productos.service;

import com.example.productos.entity.Producto;
import com.example.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo");
        }
        return productoRepository.findById(id);
    }

    public Producto create(Producto producto) {
        validar(producto);
        producto.setNombre(producto.getNombre().trim());
        return productoRepository.save(producto);
    }

    private void validar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        if (producto.getStock() == null) {
            throw new IllegalArgumentException("El stock es obligatorio");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
