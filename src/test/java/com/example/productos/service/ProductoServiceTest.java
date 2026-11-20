package com.example.productos.service;

import com.example.productos.entity.Producto;
import com.example.productos.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("findAll simula el repositorio y valida la lista recuperada")
    void findAll_simulaRepositorioYValidaListaRecuperada() {
        Producto laptop = new Producto(1L, "Laptop", new BigDecimal("15000.00"), 5);
        Producto mouse = new Producto(2L, "Mouse", new BigDecimal("250.50"), 20);
        when(productoRepository.findAll()).thenReturn(List.of(laptop, mouse));

        List<Producto> resultado = productoService.findAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Laptop");
        assertThat(resultado.get(0).getPrecio()).isEqualByComparingTo("15000.00");
        assertThat(resultado.get(0).getStock()).isEqualTo(5);
        assertThat(resultado.get(1).getNombre()).isEqualTo("Mouse");
        assertThat(resultado.get(1).getPrecio()).isEqualByComparingTo("250.50");
        assertThat(resultado.get(1).getStock()).isEqualTo(20);
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("findAll devuelve una lista vacia cuando el repositorio no tiene datos")
    void findAll_devuelveListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = productoService.findAll();

        assertThat(resultado).isEmpty();
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("create persiste un producto nuevo y verifica sus atributos")
    void create_validaPersistenciaYAtributosDelNuevoProducto() {
        Producto nuevo = new Producto("  Teclado  ", new BigDecimal("800.00"), 10);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto guardado = invocation.getArgument(0);
            guardado.setId(3L);
            return guardado;
        });

        Producto resultado = productoService.create(nuevo);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("Teclado");
        assertThat(resultado.getPrecio()).isEqualByComparingTo("800.00");
        assertThat(resultado.getStock()).isEqualTo(10);
        verify(productoRepository).save(nuevo);
    }

    @Test
    @DisplayName("create acepta stock en cero")
    void create_aceptaStockEnCero() {
        Producto nuevo = new Producto("Audifonos", new BigDecimal("499.90"), 0);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.create(nuevo);

        assertThat(resultado.getNombre()).isEqualTo("Audifonos");
        assertThat(resultado.getStock()).isZero();
        verify(productoRepository).save(nuevo);
    }

    @Test
    @DisplayName("create rechaza un producto nulo y no llama a save")
    void create_rechazaProductoNulo() {
        assertThatThrownBy(() -> productoService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El producto no puede ser nulo");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("create rechaza nombre nulo y no llama a save")
    void create_rechazaNombreNulo() {
        Producto invalido = new Producto(null, new BigDecimal("100.00"), 1);

        assertThatThrownBy(() -> productoService.create(invalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre del producto es obligatorio");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("create rechaza nombre vacio y no llama a save")
    void create_rechazaNombreVacio() {
        Producto invalido = new Producto("   ", new BigDecimal("100.00"), 1);

        assertThatThrownBy(() -> productoService.create(invalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre del producto es obligatorio");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("create rechaza precio nulo y no llama a save")
    void create_rechazaPrecioNulo() {
        Producto invalido = new Producto("Cable", null, 1);

        assertThatThrownBy(() -> productoService.create(invalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El precio debe ser mayor que cero");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("create rechaza precio cero y no llama a save")
    void create_rechazaPrecioCero() {
        Producto invalido = new Producto("Cable", BigDecimal.ZERO, 1);

        assertThatThrownBy(() -> productoService.create(invalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El precio debe ser mayor que cero");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("create rechaza precio negativo y no llama a save")
    void create_rechazaPrecioNegativo() {
        Producto invalido = new Producto("Cable", new BigDecimal("-10.00"), 1);

        assertThatThrownBy(() -> productoService.create(invalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El precio debe ser mayor que cero");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("create rechaza stock nulo y no llama a save")
    void create_rechazaStockNulo() {
        Producto invalido = new Producto("Cable", new BigDecimal("10.00"), null);

        assertThatThrownBy(() -> productoService.create(invalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El stock es obligatorio");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("create rechaza stock negativo y no llama a save")
    void create_rechazaStockNegativo() {
        Producto invalido = new Producto("Cable", new BigDecimal("10.00"), -1);

        assertThatThrownBy(() -> productoService.create(invalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El stock no puede ser negativo");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("findById recupera el producto simulado")
    void findById_recuperaProducto() {
        Producto laptop = new Producto(1L, "Laptop", new BigDecimal("15000.00"), 5);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(laptop));

        Optional<Producto> resultado = productoService.findById(1L);

        assertThat(resultado).contains(laptop);
        verify(productoRepository).findById(1L);
    }

    @Test
    @DisplayName("findById devuelve vacio cuando el producto no existe")
    void findById_devuelveVacioSiNoExiste() {
        when(productoRepository.findById(9L)).thenReturn(Optional.empty());

        assertThat(productoService.findById(9L)).isEmpty();
        verify(productoRepository).findById(9L);
    }

    @Test
    @DisplayName("findById rechaza un id nulo o no positivo")
    void findById_rechazaIdInvalido() {
        assertThatThrownBy(() -> productoService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El id debe ser positivo");
        assertThatThrownBy(() -> productoService.findById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El id debe ser positivo");
        assertThatThrownBy(() -> productoService.findById(-5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El id debe ser positivo");

        verify(productoRepository, never()).findById(any());
    }
}
