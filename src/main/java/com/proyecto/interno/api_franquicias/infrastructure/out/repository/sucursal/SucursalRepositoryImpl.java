package com.proyecto.interno.api_franquicias.infrastructure.out.repository.sucursal;

import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.port.out.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SucursalRepositoryImpl implements SucursalRepository {

    private final SucursalReactiveRepository reactiveRepository;

    @Autowired
    public SucursalRepositoryImpl(SucursalReactiveRepository reactiveRepository) {
        this.reactiveRepository = reactiveRepository;
    }

    @Override
    public Mono<Sucursal> save(Sucursal sucursal) {
        return reactiveRepository.save(sucursal);
    }

    @Override
    public Mono<Sucursal> findById(String id) {
        return reactiveRepository.findById(id);
    }

    @Override
    public Flux<Sucursal> findAll() {
        return reactiveRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return reactiveRepository.deleteById(id);
    }

    @Override
    public Flux<Sucursal> findAllByFranquiciaId(String franquiciaId) {
        return reactiveRepository.findAllByFranquiciaId(franquiciaId);
    }
    @Override
    public Mono<Sucursal> findByNombre(String nombre) {
        return reactiveRepository.findByNombre(nombre);
    }
}
