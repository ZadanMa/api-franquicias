package com.proyecto.interno.api_franquicias.domain.port.out;

import com.proyecto.interno.api_franquicias.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepository {

    Mono<Producto> save(Producto producto);

    Mono<Producto> findById(String id);

    Flux<Producto> findAll();

    Mono<Void> deleteById(String id);

    Flux<Producto> findAllBySucursalId(String sucursalId);

    Mono<Void> delete(Producto producto);

    Mono<Producto> findByNombre(String nombre);
}
