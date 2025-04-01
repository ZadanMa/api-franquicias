package com.proyecto.interno.api_franquicias.infrastructure.in.webflux.handler;

import com.proyecto.interno.api_franquicias.application.dto.FranquiciaDto;
import com.proyecto.interno.api_franquicias.application.dto.ProductoDto;
import com.proyecto.interno.api_franquicias.application.dto.ProductoStockDto;
import com.proyecto.interno.api_franquicias.application.dto.SucursalDto;
import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.port.in.FranquiciaManagementUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import com.proyecto.interno.api_franquicias.domain.exception.ErrorResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class FranquiciaHandler {
    private static final Logger log = LoggerFactory.getLogger(FranquiciaHandler.class);
    private final FranquiciaManagementUseCase service;

    public FranquiciaHandler(FranquiciaManagementUseCase service) {
        this.service = service;
    }

    public Mono<ServerResponse> registrarFranquicia(ServerRequest request) {
        return request.bodyToMono(Franquicia.class)
                .flatMap(service::registrarFranquicia)
                .flatMap(franquicia -> ServerResponse.created(
                                request.uriBuilder().path("/{id}").build(franquicia.getId()))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(franquicia))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> actualizarNombreFranquicia(ServerRequest request) {
        return Mono.just(request.pathVariable("franquiciaId"))
                .flatMap(franquiciaId -> request.bodyToMono(FranquiciaDto.class)
                        .flatMap(dto -> service.actualizarNombreFranquicia(franquiciaId, dto.getNuevoFranquicia())))
                .flatMap(franquicia -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(franquicia))
                .onErrorResume(e -> e.getMessage().contains("no encontrada")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.badRequest().bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> getFranquiciaCompleta(ServerRequest request) {
        return Mono.just(request.pathVariable("franquiciaId"))
                .flatMap(service::getFranquiciaCompleta)
                .flatMap(franquicia -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(franquicia))
                .onErrorResume(e -> e.getMessage().contains("no encontrada")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> productoConMasStockPorSucursal(ServerRequest request) {
        return Mono.just(request.pathVariable("franquiciaId"))
                .flatMapMany(service::productoConMasStockPorSucursal)
                .collectList()
                .flatMap(list -> list.isEmpty()
                        ? ServerResponse.noContent().build()
                        : ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(list))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> agregarSucursal(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");

        return request.bodyToMono(Sucursal.class)
                .flatMap(sucursal -> service.agregarSucursal(franquiciaId, sucursal))
                .flatMap(sucursal -> ServerResponse.created(
                                request.uriBuilder().path("/{id}").build(sucursal.getId()))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(sucursal))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(new ErrorResponse(e.getMessage())))
                .onErrorResume(e -> e.getMessage().contains("no encontrada")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> registrarSucursal(ServerRequest request) {
        return request.bodyToMono(Sucursal.class)
                .flatMap(service::registrarSucursal)
                .flatMap(sucursal -> ServerResponse.created(
                                request.uriBuilder().path("/{id}").build(sucursal.getId()))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(sucursal))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> asociarSucursalAFranquicia(ServerRequest request) {
        return Mono.zip(
                        Mono.just(request.pathVariable("franquiciaId")),
                        Mono.just(request.pathVariable("sucursalId"))
                )
                .flatMap(tuple -> service.asociarSucursalAFranquicia(tuple.getT1(), tuple.getT2()))
                .flatMap(sucursal -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(sucursal))
                .onErrorResume(e -> e.getMessage().contains("no encontrada")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> getAllSucursales(ServerRequest request) {
        log.info("🔵 Recibida solicitud para obtener todas las sucursales");
        return service.getAllSucursales()
                .collectList()
                .flatMap(list -> {
                    log.info("🟢 Cantidad de sucursales encontradas: {}", list.size());
                    return list.isEmpty()
                            ? ServerResponse.noContent().build()
                            : ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(list);
                })
                .doOnError(error -> log.error("❌ Error al obtener sucursales", error));
    }

    public Mono<ServerResponse> getSucursalById(ServerRequest request) {
        return Mono.just(request.pathVariable("sucursalId"))
                .flatMap(service::getSucursalById)
                .flatMap(sucursal -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(sucursal))
                .onErrorResume(e -> e.getMessage().contains("no encontrada")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> actualizarNombreSucursal(ServerRequest request) {
        String sucursalId = request.pathVariable("sucursalId");

        return request.bodyToMono(SucursalDto.class)
                .map(SucursalDto::getNombreSucursal)
                .flatMap(nombre -> service.actualizarNombreSucursal(sucursalId, nombre))
                .flatMap(sucursal -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(sucursal))
                .onErrorResume(e -> e.getMessage().contains("no encontrada")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }


    public Mono<ServerResponse> agregarProducto(ServerRequest request) {
        return Mono.just(request.pathVariable("sucursalId"))
                .flatMap(sucursalId ->
                        request.bodyToMono(Producto.class)
                                .flatMap(producto -> service.agregarProducto(sucursalId, producto))
                )
                .flatMap(producto -> ServerResponse.created(
                                request.uriBuilder().path("/{id}").build(producto.getId()))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(producto))
                .onErrorResume(e -> e.getMessage().contains("no encontrada")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.badRequest().bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> registrarProducto(ServerRequest request) {
        return request.bodyToMono(Producto.class)
                .flatMap(service::registrarProducto)
                .flatMap(producto -> ServerResponse.created(
                                request.uriBuilder().path("/{id}").build(producto.getId()))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(producto))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> asociarProductoASucursal(ServerRequest request) {
        return Mono.zip(
                        Mono.just(request.pathVariable("sucursalId")),
                        Mono.just(request.pathVariable("productoId"))
                )
                .flatMap(tuple -> service.asociarProductoASucursal(tuple.getT1(), tuple.getT2()))
                .flatMap(producto -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(producto))
                .onErrorResume(e -> e.getMessage().contains("no encontrado")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> getAllProductos(ServerRequest request) {
        return service.getAllProductos()
                .collectList()
                .flatMap(list -> list.isEmpty()
                        ? ServerResponse.noContent().build()
                        : ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(list));
    }

    public Mono<ServerResponse> getProductoById(ServerRequest request) {
        return Mono.just(request.pathVariable("productoId"))
                .flatMap(service::getProductoById)
                .flatMap(producto -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(producto))
                .onErrorResume(e -> e.getMessage().contains("no encontrado")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

    public Mono<ServerResponse> eliminarProducto(ServerRequest request) {
        String productoId = request.pathVariable("productoId");
        return service.eliminarProducto(productoId)
                .then(ServerResponse.noContent().build())
                .onErrorResume(e -> e.getMessage().contains("no encontrado")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }


    public Mono<ServerResponse> actualizarStockProducto(ServerRequest request) {
        return request.bodyToMono(ProductoStockDto.class)
                .flatMap(dto -> service.modificarStockProducto(request.pathVariable("productoId"), dto.getNuevoStock()))
                .flatMap(producto -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(producto))
                .onErrorResume(e -> e.getMessage().contains("no encontrado")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }


    public Mono<ServerResponse> actualizarNombreProducto(ServerRequest request) {
        return request.bodyToMono(ProductoDto.class)
                .flatMap(dto -> service.actualizarNombreProducto(request.pathVariable("productoId"), dto.getNombreProducto()))
                .flatMap(producto -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(producto))
                .onErrorResume(e -> e.getMessage().contains("no encontrado")
                        ? ServerResponse.notFound().build()
                        : ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(new ErrorResponse(e.getMessage())));
    }

}