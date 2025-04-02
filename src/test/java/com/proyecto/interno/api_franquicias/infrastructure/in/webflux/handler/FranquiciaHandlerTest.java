package com.proyecto.interno.api_franquicias.infrastructure.in.webflux.handler;

import com.proyecto.interno.api_franquicias.application.dto.*;
import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.port.in.FranquiciaManagementUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

class FranquiciaHandlerTest {

    private WebTestClient webTestClient;
    private FranquiciaHandler handler;
    private FranquiciaManagementUseCase service;

    @BeforeEach
    void setUp() {
        service = mock(FranquiciaManagementUseCase.class);
        handler = new FranquiciaHandler(service);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions
                .route(POST("/franquicias"), handler::registrarFranquicia)
                .andRoute(PUT("/franquicias/{franquiciaId}"), handler::actualizarNombreFranquicia)
                .andRoute(GET("/franquicias/{franquiciaId}"), handler::getFranquiciaCompleta)
                .andRoute(GET("/franquicias/{franquiciaId}/productos/mas-stock"), handler::productoConMasStockPorSucursal)
                .andRoute(POST("/franquicias/{franquiciaId}/sucursales"), handler::agregarSucursal)
                .andRoute(POST("/sucursales"), handler::registrarSucursal)
                .andRoute(PUT("/franquicias/{franquiciaId}/sucursales/{sucursalId}"), handler::asociarSucursalAFranquicia)
                .andRoute(GET("/sucursales"), handler::getAllSucursales)
                .andRoute(GET("/sucursales/{sucursalId}"), handler::getSucursalById)
                .andRoute(PUT("/sucursales/{sucursalId}"), handler::actualizarNombreSucursal)
                .andRoute(POST("/sucursales/{sucursalId}/productos"), handler::agregarProducto)
                .andRoute(POST("/productos"), handler::registrarProducto)
                .andRoute(PUT("/sucursales/{sucursalId}/productos/{productoId}"), handler::asociarProductoASucursal)
                .andRoute(GET("/productos"), handler::getAllProductos)
                .andRoute(GET("/productos/{productoId}"), handler::getProductoById)
                .andRoute(DELETE("/productos/{productoId}"), handler::eliminarProducto)
                .andRoute(PUT("/productos/{productoId}/stock"), handler::actualizarStockProducto)
                .andRoute(PUT("/productos/{productoId}/nombre"), handler::actualizarNombreProducto)
                .andRoute(GET("/health"), handler::healthCheck);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void registrarFranquicia() {
        Franquicia franquicia = new Franquicia();
        franquicia.setId("1");
        franquicia.setNombre("Franquicia Test");
        when(service.registrarFranquicia(any(Franquicia.class))).thenReturn(Mono.just(franquicia));
        webTestClient.post().uri("/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(franquicia)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Franquicia Test");
    }

    @Test
    void actualizarNombreFranquicia() {
        Franquicia franquicia = new Franquicia();
        franquicia.setId("1");
        franquicia.setNombre("Franquicia Actualizada");
        FranquiciaDto franquiciaDto = new FranquiciaDto();
        franquiciaDto.setNuevoFranquicia("Franquicia Actualizada");
        when(service.actualizarNombreFranquicia(anyString(), anyString())).thenReturn(Mono.just(franquicia));
        webTestClient.put().uri("/franquicias/{franquiciaId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(franquiciaDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Franquicia Actualizada");
    }

    @Test
    void getFranquiciaCompleta() {
        FranquiciaDetailsDTO franquiciaDetailsDTO = new FranquiciaDetailsDTO();
        franquiciaDetailsDTO.setId("1");
        franquiciaDetailsDTO.setNombre("Franquicia Test");

        when(service.getFranquiciaCompleta(anyString())).thenReturn(Mono.just(franquiciaDetailsDTO));

        webTestClient.get().uri("/franquicias/{franquiciaId}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Franquicia Test");
    }
    @Test
    void productoConMasStockPorSucursal() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sucursalId", "1");
        result.put("sucursalNombre", "Sucursal Test");
        result.put("producto", null);

        when(service.productoConMasStockPorSucursal(anyString())).thenReturn(Flux.just(result));

        webTestClient.get().uri("/franquicias/{franquiciaId}/productos/mas-stock", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].sucursalId").isEqualTo("1")
                .jsonPath("$[0].sucursalNombre").isEqualTo("Sucursal Test")
                .jsonPath("$[0].producto").isEmpty();
    }
    @Test
    void agregarSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId("1");
        sucursal.setNombre("Sucursal Test");

        when(service.agregarSucursal(anyString(), any(Sucursal.class))).thenReturn(Mono.just(sucursal));

        webTestClient.post().uri("/franquicias/{franquiciaId}/sucursales", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sucursal)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Sucursal Test");
    }
    @Test
    void registrarSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId("1");
        sucursal.setNombre("Sucursal Test");

        when(service.registrarSucursal(any(Sucursal.class))).thenReturn(Mono.just(sucursal));

        webTestClient.post().uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sucursal)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Sucursal Test");
    }
    @Test
    void asociarSucursalAFranquicia() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId("1");
        sucursal.setNombre("Sucursal Test");

        when(service.asociarSucursalAFranquicia(anyString(), anyString())).thenReturn(Mono.just(sucursal));

        webTestClient.put().uri("/franquicias/{franquiciaId}/sucursales/{sucursalId}", "1", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Sucursal Test");
    }
    @Test
    void getAllSucursales() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId("1");
        sucursal.setNombre("Sucursal Test");

        when(service.getAllSucursales()).thenReturn(Flux.just(sucursal));

        webTestClient.get().uri("/sucursales")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].nombre").isEqualTo("Sucursal Test");
    }
    @Test
    void getSucursalById() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId("1");
        sucursal.setNombre("Sucursal Test");

        when(service.getSucursalById(anyString())).thenReturn(Mono.just(sucursal));

        webTestClient.get().uri("/sucursales/{sucursalId}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Sucursal Test");
    }
    @Test
    void actualizarNombreSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId("1");
        sucursal.setNombre("Sucursal Actualizada");

        SucursalDto sucursalDto = new SucursalDto();
        sucursalDto.setNombreSucursal("Sucursal Actualizada");

        when(service.actualizarNombreSucursal(anyString(), anyString())).thenReturn(Mono.just(sucursal));

        webTestClient.put().uri("/sucursales/{sucursalId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sucursalDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Sucursal Actualizada");
    }
    @Test
    void agregarProducto() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");

        when(service.agregarProducto(anyString(), any(Producto.class))).thenReturn(Mono.just(producto));

        webTestClient.post().uri("/sucursales/{sucursalId}/productos", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(producto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Producto Test");
    }
    @Test
    void registrarProducto() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");

        when(service.registrarProducto(any(Producto.class))).thenReturn(Mono.just(producto));

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(producto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Producto Test");
    }
    @Test
    void asociarProductoASucursal() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");

        when(service.asociarProductoASucursal(anyString(), anyString())).thenReturn(Mono.just(producto));

        webTestClient.put().uri("/sucursales/{sucursalId}/productos/{productoId}", "1", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Producto Test");
    }
    @Test
    void getAllProductos() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");

        when(service.getAllProductos()).thenReturn(Flux.just(producto));

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].nombre").isEqualTo("Producto Test");
    }
    @Test
    void getProductoById() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");

        when(service.getProductoById(anyString())).thenReturn(Mono.just(producto));

        webTestClient.get().uri("/productos/{productoId}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Producto Test");
    }
    @Test
    void eliminarProducto() {
        when(service.eliminarProducto(anyString())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/productos/{productoId}", "1")
                .exchange()
                .expectStatus().isNoContent();
    }@Test
    void actualizarStockProducto() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");
        producto.setStock(100);

        ProductoStockDto productoStockDto = new ProductoStockDto();
        productoStockDto.setNuevoStock(100);

        when(service.modificarStockProducto(anyString(), anyInt())).thenReturn(Mono.just(producto));

        webTestClient.put().uri("/productos/{productoId}/stock", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productoStockDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Producto Test")
                .jsonPath("$.stock").isEqualTo(100);
    }
    @Test
    void actualizarNombreProducto() {
        Producto producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Actualizado");

        ProductoDto productoDto = new ProductoDto();
        productoDto.setNombreProducto("Producto Actualizado");

        when(service.actualizarNombreProducto(anyString(), anyString())).thenReturn(Mono.just(producto));

        webTestClient.put().uri("/productos/{productoId}/nombre", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productoDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombre").isEqualTo("Producto Actualizado");
    }
    @Test
    void healthCheck() {
        webTestClient.get().uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP")
                .jsonPath("$.service").isEqualTo("Franquicias API")
                .jsonPath("$.timestamp").exists();
    }

}