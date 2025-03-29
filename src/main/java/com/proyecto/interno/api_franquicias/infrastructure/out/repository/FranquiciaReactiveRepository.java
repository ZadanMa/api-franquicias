package com.proyecto.interno.api_franquicias.infrastructure.out.repository;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranquiciaReactiveRepository extends ReactiveMongoRepository<Franquicia, String> {
}
