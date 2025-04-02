package com.proyecto.interno.api_franquicias.infrastructure.out.repository.producto;

import com.proyecto.interno.api_franquicias.domain.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoRepositoryImplTest {

    @Mock
    private ProductoReactiveRepository reactiveRepository;

    @InjectMocks
    private ProductoRepositoryImpl productoRepository;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId("1");
        producto.setNombre("Producto Test");
        producto.setStock(10);
        producto.setSucursalId("sucursal-123");
    }

    @Test
    void save_DebeGuardarProducto() {
        when(reactiveRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(productoRepository.save(producto))
                .expectNext(producto)
                .verifyComplete();

        verify(reactiveRepository).save(producto);
    }

    @Test
    void findById_DebeEncontrarProductoPorId() {
        when(reactiveRepository.findById(anyString())).thenReturn(Mono.just(producto));

        StepVerifier.create(productoRepository.findById("1"))
                .expectNext(producto)
                .verifyComplete();

        verify(reactiveRepository).findById("1");
    }

    @Test
    void findAll_DebeRetornarTodosLosProductos() {
        Producto otroProducto = new Producto();
        otroProducto.setId("2");
        otroProducto.setNombre("Otro Producto");
        otroProducto.setStock(5);
        otroProducto.setSucursalId("sucursal-123");

        when(reactiveRepository.findAll()).thenReturn(Flux.just(producto, otroProducto));

        StepVerifier.create(productoRepository.findAll())
                .expectNext(producto)
                .expectNext(otroProducto)
                .verifyComplete();

        verify(reactiveRepository).findAll();
    }

    @Test
    void deleteById_DebeEliminarProductoPorId() {
        when(reactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productoRepository.deleteById("1"))
                .verifyComplete();

        verify(reactiveRepository).deleteById("1");
    }

    @Test
    void findAllBySucursalId_DebeEncontrarProductosPorSucursalId() {
        Producto otroProducto = new Producto();
        otroProducto.setId("2");
        otroProducto.setNombre("Otro Producto");
        otroProducto.setStock(5);
        otroProducto.setSucursalId("sucursal-123");

        when(reactiveRepository.findAllBySucursalId(anyString())).thenReturn(Flux.just(producto, otroProducto));

        StepVerifier.create(productoRepository.findAllBySucursalId("sucursal-123"))
                .expectNext(producto)
                .expectNext(otroProducto)
                .verifyComplete();

        verify(reactiveRepository).findAllBySucursalId("sucursal-123");
    }

    @Test
    void delete_DebeEliminarProducto() {
        when(reactiveRepository.delete(any(Producto.class))).thenReturn(Mono.empty());

        StepVerifier.create(productoRepository.delete(producto))
                .verifyComplete();

        verify(reactiveRepository).delete(producto);
    }

    @Test
    void findByNombre_DebeEncontrarProductoPorNombre() {
        when(reactiveRepository.findByNombre(anyString())).thenReturn(Mono.just(producto));

        StepVerifier.create(productoRepository.findByNombre("Producto Test"))
                .expectNext(producto)
                .verifyComplete();

        verify(reactiveRepository).findByNombre("Producto Test");
    }
}