package com.proyecto.interno.api_franquicias.infrastructure.out.repository.franquicia;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.port.out.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FranquiciaRepositoryImplTest {

    @Mock
    private FranquiciaReactiveRepository reactiveRepository;

    private FranquiciaRepository franquiciaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        franquiciaRepository = new FranquiciaRepositoryImpl(reactiveRepository);
    }

    @Test
    void testSave() {
        Franquicia franquicia = new Franquicia("1", "Franquicia Uno");
        when(reactiveRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        Mono<Franquicia> result = franquiciaRepository.save(franquicia);
        StepVerifier.create(result)
                .expectNext(franquicia)
                .verifyComplete();
    }

    @Test
    void testFindById() {
        Franquicia franquicia = new Franquicia("1", "Franquicia Uno");
        when(reactiveRepository.findById("1")).thenReturn(Mono.just(franquicia));

        Mono<Franquicia> result = franquiciaRepository.findById("1");
        StepVerifier.create(result)
                .expectNext(franquicia)
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        Franquicia f1 = new Franquicia("1", "Franquicia Uno");
        Franquicia f2 = new Franquicia("2", "Franquicia Dos");
        when(reactiveRepository.findAll()).thenReturn(Flux.just(f1, f2));

        Flux<Franquicia> result = franquiciaRepository.findAll();
        StepVerifier.create(result)
                .expectNext(f1, f2)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(reactiveRepository.deleteById("1")).thenReturn(Mono.empty());

        Mono<Void> result = franquiciaRepository.deleteById("1");
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testFindByNombre() {
        Franquicia franquicia = new Franquicia("1", "Franquicia Uno");
        when(reactiveRepository.findByNombre("Franquicia Uno")).thenReturn(Mono.just(franquicia));

        Mono<Franquicia> result = franquiciaRepository.findByNombre("Franquicia Uno");
        StepVerifier.create(result)
                .expectNext(franquicia)
                .verifyComplete();
    }
}
