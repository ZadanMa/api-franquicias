package com.proyecto.interno.api_franquicias.infrastructure.out.repository.franquicia;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranquiciaReactiveRepository extends ReactiveMongoRepository<Franquicia, String> {
    Mono<Franquicia> findByNombre(String nombre);
}
