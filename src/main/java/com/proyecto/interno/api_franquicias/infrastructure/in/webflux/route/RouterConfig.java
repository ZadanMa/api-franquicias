package com.proyecto.interno.api_franquicias.infrastructure.in.webflux.route;

import com.proyecto.interno.api_franquicias.infrastructure.in.webflux.handler.FranquiciaHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class RouterConfig {

    private static final Logger log = LoggerFactory.getLogger(RouterConfig.class);

    @PostConstruct
    public void init() {
        log.info("RouterConfig funcionaa");
    }
    @Bean
    public RouterFunction<ServerResponse> healthRoute(FranquiciaHandler handler) {
        return route(GET("/health"), handler::healthCheck)
                .andRoute(GET("/actuator/health"), handler::healthCheck); // Opcional para compatibilidad con estándares
    }
    @Bean
    public RouterFunction<ServerResponse> franquiciaRoutes(FranquiciaHandler handler) {
        return RouterFunctions.nest(path("/api/franquicias"),
                route(POST("").and(accept(APPLICATION_JSON)), handler::registrarFranquicia)
                .andRoute(PUT("/{franquiciaId}").and(accept(APPLICATION_JSON)), handler::actualizarNombreFranquicia)
                .andRoute(GET("/{franquiciaId}/info").and(accept(APPLICATION_JSON)), handler::getFranquiciaCompleta)
                .andRoute(GET("/{franquiciaId}/mas-stock").and(accept(APPLICATION_JSON)), handler::productoConMasStockPorSucursal)
                .andRoute(POST("/{franquiciaId}/sucursales").and(accept(APPLICATION_JSON)), handler::agregarSucursal)
                .andRoute(PUT("/sucursales/{sucursalId}").and(accept(APPLICATION_JSON)), handler::actualizarNombreSucursal)
                .andRoute(POST("/sucursales").and(accept(APPLICATION_JSON)), handler::registrarSucursal)
                .andRoute(PUT("/{franquiciaId}/sucursales/asociar/{sucursalId}").and(accept(APPLICATION_JSON)), handler::asociarSucursalAFranquicia)
                .andRoute(GET("/sucursales"), handler::getAllSucursales)
                .andRoute(GET("/sucursales/{sucursalId}").and(accept(APPLICATION_JSON)), handler::getSucursalById)
                .andRoute(POST("/sucursales/{sucursalId}/productos").and(accept(APPLICATION_JSON)), handler::agregarProducto)
                .andRoute(POST("/productos").and(accept(APPLICATION_JSON)), handler::registrarProducto)
                .andRoute(PUT("/sucursales/{sucursalId}/productos/asociar/{productoId}").and(accept(APPLICATION_JSON)), handler::asociarProductoASucursal)
                .andRoute(GET("/productos").and(accept(APPLICATION_JSON)), handler::getAllProductos)
                .andRoute(GET("/productos/{productoId}").and(accept(APPLICATION_JSON)), handler::getProductoById)
                .andRoute(DELETE("/productos/{productoId}"), handler::eliminarProducto)
                .andRoute(PUT("/productos/{productoId}/stock").and(accept(APPLICATION_JSON)), handler::actualizarStockProducto)
                .andRoute(PUT("/productos/{productoId}/nombre").and(accept(APPLICATION_JSON)), handler::actualizarNombreProducto));
    }
}