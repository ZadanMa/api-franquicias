package com.proyecto.interno.api_franquicias.infrastructure.out.repository.producto;

import com.proyecto.interno.api_franquicias.domain.model.Producto;
import com.proyecto.interno.api_franquicias.domain.port.out.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductoRepositoryImpl implements ProductoRepository {

    private final ProductoReactiveRepository reactiveRepository;

    @Autowired
    public ProductoRepositoryImpl(ProductoReactiveRepository reactiveRepository) {
        this.reactiveRepository = reactiveRepository;
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return reactiveRepository.save(producto);
    }

    @Override
    public Mono<Producto> findById(String id) {
        return reactiveRepository.findById(id);
    }

    @Override
    public Flux<Producto> findAll() {
        return reactiveRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return reactiveRepository.deleteById(id);
    }

    @Override
    public Flux<Producto> findAllBySucursalId(String sucursalId) {
        return reactiveRepository.findAllBySucursalId(sucursalId);
    }
    @Override
    public Mono<Void> delete(Producto producto) {
        return reactiveRepository.delete(producto);
    }
}
