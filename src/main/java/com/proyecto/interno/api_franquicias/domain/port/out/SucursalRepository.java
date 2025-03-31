package com.proyecto.interno.api_franquicias.domain.port.out;

import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepository {

    Mono<Sucursal> save(Sucursal sucursal);
    Mono<Sucursal> findById(String id);
    Flux<Sucursal> findAll();
    Mono<Void> deleteById(String id);
    Flux<Sucursal> findAllByFranquiciaId(String franquiciaId);
}
