package com.example.productos.controller;

import com.example.productos.entity.Producto;
import com.example.productos.service.ProductoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@Import(ApiExceptionHandler.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Test
    @DisplayName("GET /api/productos devuelve la lista del servicio")
    void findAll_devuelveLista() throws Exception {
        Producto laptop = new Producto(1L, "Laptop", new BigDecimal("15000.00"), 5);
        when(productoService.findAll()).thenReturn(List.of(laptop));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                .andExpect(jsonPath("$[0].precio").value(15000.00))
                .andExpect(jsonPath("$[0].stock").value(5));

        verify(productoService).findAll();
    }

    @Test
    @DisplayName("GET /api/productos/{id} devuelve el producto")
    void findById_devuelveProducto() throws Exception {
        Producto mouse = new Producto(2L, "Mouse", new BigDecimal("250.50"), 20);
        when(productoService.findById(2L)).thenReturn(Optional.of(mouse));

        mockMvc.perform(get("/api/productos/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mouse"))
                .andExpect(jsonPath("$.stock").value(20));
    }

    @Test
    @DisplayName("GET /api/productos/{id} responde 404 si no existe")
    void findById_responde404() throws Exception {
        when(productoService.findById(9L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/productos crea el producto y responde 201")
    void create_responde201() throws Exception {
        Producto creado = new Producto(3L, "Teclado", new BigDecimal("800.00"), 10);
        when(productoService.create(any(Producto.class))).thenReturn(creado);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Teclado","precio":800.00,"stock":10}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Teclado"))
                .andExpect(jsonPath("$.precio").value(800.00))
                .andExpect(jsonPath("$.stock").value(10));

        verify(productoService).create(any(Producto.class));
    }

    @Test
    @DisplayName("POST /api/productos responde 400 cuando la validacion falla")
    void create_responde400CuandoLaValidacionFalla() throws Exception {
        when(productoService.create(any(Producto.class)))
                .thenThrow(new IllegalArgumentException("El precio debe ser mayor que cero"));

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Cable","precio":0,"stock":1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El precio debe ser mayor que cero"));
    }
}
