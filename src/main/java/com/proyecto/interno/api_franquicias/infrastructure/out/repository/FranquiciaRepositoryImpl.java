package com.proyecto.interno.api_franquicias.infrastructure.out.repository;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.port.out.FranquiciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FranquiciaRepositoryImpl implements FranquiciaRepository {

    private final FranquiciaReactiveRepository reactiveRepository;

    @Autowired
    public FranquiciaRepositoryImpl(FranquiciaReactiveRepository reactiveRepository) {
        this.reactiveRepository = reactiveRepository;
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        return reactiveRepository.save(franquicia);
    }

    @Override
    public Mono<Franquicia> findById(String id) {
        return reactiveRepository.findById(id);
    }

    @Override
    public Flux<Franquicia> findAll() {
        return reactiveRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return reactiveRepository.deleteById(id);
    }
}
