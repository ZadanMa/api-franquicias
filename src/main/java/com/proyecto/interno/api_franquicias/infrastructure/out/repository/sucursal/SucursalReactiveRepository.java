package com.proyecto.interno.api_franquicias.infrastructure.out.repository.sucursal;

import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalReactiveRepository extends ReactiveMongoRepository<Sucursal, String> {
    Flux<Sucursal> findAllByFranquiciaId(String franquiciaId);
}
