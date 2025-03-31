package com.proyecto.interno.api_franquicias.infrastructure.in.webflux.route;

import com.proyecto.interno.api_franquicias.infrastructure.in.webflux.handler.FranquiciaHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class RouterConfig {

    private static final Logger log = LoggerFactory.getLogger(RouterConfig.class);

    @PostConstruct
    public void init() {
        log.info("✅ RouterConfig cargado correctamente");
    }
    @Bean
    public RouterFunction<ServerResponse> franquiciaRoutes(FranquiciaHandler handler) {
        return route(POST("/api/franquicias").and(accept(APPLICATION_JSON)), handler::registrarFranquicia)
                .andRoute(PUT("/api/franquicias/{franquiciaId}").and(accept(APPLICATION_JSON)), handler::actualizarNombreFranquicia)
                .andRoute(GET("/api/franquicias/{franquiciaId}/info").and(accept(APPLICATION_JSON)), handler::getFranquiciaCompleta)
                .andRoute(GET("/api/franquicias/{franquiciaId}/mas-stock").and(accept(APPLICATION_JSON)), handler::productoConMasStockPorSucursal)
                .andRoute(POST("/api/franquicias/{franquiciaId}/sucursales").and(accept(APPLICATION_JSON)), handler::agregarSucursal)
                .andRoute(PUT("/api/franquicias/sucursales/{sucursalId}").and(accept(APPLICATION_JSON)), handler::actualizarNombreSucursal)
                .andRoute(POST("/api/franquicias/sucursales").and(accept(APPLICATION_JSON)), handler::registrarSucursal)
                .andRoute(PUT("/api/franquicias/{franquiciaId}/sucursales/asociar/{sucursalId}").and(accept(APPLICATION_JSON)), handler::asociarSucursalAFranquicia)
                .andRoute(GET("/api/franquicias/sucursales"), handler::getAllSucursales)
                .andRoute(GET("/api/franquicias/sucursales/{sucursalId}").and(accept(APPLICATION_JSON)), handler::getSucursalById)
                .andRoute(POST("/api/franquicias/sucursales/{sucursalId}/productos").and(accept(APPLICATION_JSON)), handler::agregarProducto)
                .andRoute(POST("/api/franquicias/productos").and(accept(APPLICATION_JSON)), handler::registrarProducto)
                .andRoute(PUT("/api/franquicias/sucursales/{sucursalId}/productos/asociar/{productoId}").and(accept(APPLICATION_JSON)), handler::asociarProductoASucursal)
                .andRoute(GET("/api/franquicias/productos").and(accept(APPLICATION_JSON)), handler::getAllProductos)
                .andRoute(GET("/api/franquicias/productos/{productoId}").and(accept(APPLICATION_JSON)), handler::getProductoById)
                .andRoute(DELETE("/api/franquicias/sucursales/{sucursalId}/productos/{productoId}"), handler::eliminarProducto)
                .andRoute(PUT("/api/franquicias/productos/{productoId}/stock").and(accept(APPLICATION_JSON)), handler::actualizarStockProducto)
                .andRoute(PUT("/api/franquicias/productos/{productoId}/nombre").and(accept(APPLICATION_JSON)), handler::actualizarNombreProducto);
    }
}