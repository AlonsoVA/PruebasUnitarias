package com.example.productos.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoTest {

    @Test
    @DisplayName("constructores, getters y setters conservan los atributos")
    void constructoresGettersYSetters() {
        Producto vacio = new Producto();
        assertThat(vacio.getId()).isNull();
        assertThat(vacio.getNombre()).isNull();
        assertThat(vacio.getPrecio()).isNull();
        assertThat(vacio.getStock()).isNull();

        Producto producto = new Producto("Monitor", new BigDecimal("3000.00"), 4);
        assertThat(producto.getNombre()).isEqualTo("Monitor");
        assertThat(producto.getPrecio()).isEqualByComparingTo("3000.00");
        assertThat(producto.getStock()).isEqualTo(4);

        producto.setId(8L);
        producto.setNombre("Monitor 24");
        producto.setPrecio(new BigDecimal("3200.50"));
        producto.setStock(6);

        assertThat(producto.getId()).isEqualTo(8L);
        assertThat(producto.getNombre()).isEqualTo("Monitor 24");
        assertThat(producto.getPrecio()).isEqualByComparingTo("3200.50");
        assertThat(producto.getStock()).isEqualTo(6);

        Producto completo = new Producto(8L, "Monitor 24", new BigDecimal("3200.50"), 6);
        assertThat(completo.getId()).isEqualTo(8L);
        assertThat(completo.toString()).contains("Monitor 24").contains("3200.50");
    }
}
