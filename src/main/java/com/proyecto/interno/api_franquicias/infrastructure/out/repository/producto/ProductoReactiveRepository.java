package com.proyecto.interno.api_franquicias.infrastructure.out.repository.producto;

import com.proyecto.interno.api_franquicias.domain.model.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductoReactiveRepository extends ReactiveMongoRepository<Producto, String> {
    Flux<Producto> findAllBySucursalId(String sucursalId);
    Mono<Producto> findByNombre(String nombre);
}
