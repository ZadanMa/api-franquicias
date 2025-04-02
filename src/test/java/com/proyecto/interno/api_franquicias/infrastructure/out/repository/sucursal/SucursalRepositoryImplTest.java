package com.proyecto.interno.api_franquicias.infrastructure.out.repository.sucursal;

import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.port.out.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SucursalRepositoryImplTest {

    @Mock
    private SucursalReactiveRepository reactiveRepository;

    private SucursalRepository sucursalRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sucursalRepository = new SucursalRepositoryImpl(reactiveRepository);
    }

    @Test
    void testSave() {
        Sucursal sucursal = new Sucursal("1", "F1", "Sucursal Uno");
        when(reactiveRepository.save(any(Sucursal.class))).thenReturn(Mono.just(sucursal));

        Mono<Sucursal> result = sucursalRepository.save(sucursal);
        StepVerifier.create(result)
                .expectNext(sucursal)
                .verifyComplete();
    }

    @Test
    void testFindById() {
        Sucursal sucursal = new Sucursal("1", "F1", "Sucursal Uno");
        when(reactiveRepository.findById("1")).thenReturn(Mono.just(sucursal));

        Mono<Sucursal> result = sucursalRepository.findById("1");
        StepVerifier.create(result)
                .expectNext(sucursal)
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        Sucursal s1 = new Sucursal("1", "F1", "Sucursal Uno");
        Sucursal s2 = new Sucursal("2", "F2", "Sucursal Dos");
        when(reactiveRepository.findAll()).thenReturn(Flux.just(s1, s2));

        Flux<Sucursal> result = sucursalRepository.findAll();
        StepVerifier.create(result)
                .expectNext(s1, s2)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(reactiveRepository.deleteById("1")).thenReturn(Mono.empty());

        Mono<Void> result = sucursalRepository.deleteById("1");
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testFindAllByFranquiciaId() {
        Sucursal s1 = new Sucursal("1", "F1", "Sucursal Uno");
        Sucursal s2 = new Sucursal("2", "F1", "Sucursal Dos");
        when(reactiveRepository.findAllByFranquiciaId("F1")).thenReturn(Flux.just(s1, s2));

        Flux<Sucursal> result = sucursalRepository.findAllByFranquiciaId("F1");
        StepVerifier.create(result)
                .expectNext(s1, s2)
                .verifyComplete();
    }

    @Test
    void testFindByNombre() {
        Sucursal sucursal = new Sucursal("1", "F1", "Sucursal Uno");
        when(reactiveRepository.findByNombre("Sucursal Uno")).thenReturn(Mono.just(sucursal));

        Mono<Sucursal> result = sucursalRepository.findByNombre("Sucursal Uno");
        StepVerifier.create(result)
                .expectNext(sucursal)
                .verifyComplete();
    }
}
