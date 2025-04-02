package com.proyecto.interno.api_franquicias.application.service;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.port.out.FranquiciaRepository;
import com.proyecto.interno.api_franquicias.domain.port.out.ProductoRepository;
import com.proyecto.interno.api_franquicias.domain.port.out.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceImplTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private FranquiciaServiceImpl franquiciaService;

    private Producto producto;
    private Sucursal sucursal;
    private Franquicia franquicia;
    private final String productoId = "producto-123";
    private final String sucursalId = "sucursal-123";
    private final String franquiciaId = "franquicia-123";

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(productoId);
        producto.setNombre("Producto Test");
        producto.setStock(10);
        producto.setSucursalId(sucursalId);

        sucursal = new Sucursal();
        sucursal.setId(sucursalId);
        sucursal.setNombre("Sucursal Test");
        sucursal.setFranquiciaId(franquiciaId);

        franquicia = new Franquicia();
        franquicia.setId(franquiciaId);
        franquicia.setNombre("Franquicia Test");
    }

    @Test
    void agregarProducto_ConDatosValidos_DebeGuardarProducto() {
        when(sucursalRepository.findById(anyString())).thenReturn(Mono.just(sucursal));
        when(productoRepository.findByNombre(anyString())).thenReturn(Mono.empty());
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(franquiciaService.agregarProducto(sucursalId, producto))
                .expectNext(producto)
                .verifyComplete();

        verify(sucursalRepository).findById(sucursalId);
        verify(productoRepository).findByNombre(producto.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void agregarProducto_ConProductoNull_DebeLanzarError() {
        StepVerifier.create(franquiciaService.agregarProducto(sucursalId, null))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El producto no puede ser null"))
                .verify();
    }

    @Test
    void agregarProducto_ConNombreVacio_DebeLanzarError() {
        producto.setNombre("");

        StepVerifier.create(franquiciaService.agregarProducto(sucursalId, producto))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El nombre del producto es requerido"))
                .verify();
    }

    @Test
    void agregarProducto_ConStockNegativo_DebeLanzarError() {
        producto.setStock(-5);

        StepVerifier.create(franquiciaService.agregarProducto(sucursalId, producto))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El stock del producto es nulo o es negativo"))
                .verify();
    }

    @Test
    void registrarProducto_ConDatosValidos_DebeGuardarProducto() {
        when(productoRepository.findByNombre(anyString())).thenReturn(Mono.empty());
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(franquiciaService.registrarProducto(producto))
                .expectNext(producto)
                .verifyComplete();

        verify(productoRepository).findByNombre(producto.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void asociarProductoASucursal_ConDatosValidos_DebeAsociarProducto() {
        when(sucursalRepository.findById(anyString())).thenReturn(Mono.just(sucursal));
        when(productoRepository.findById(anyString())).thenReturn(Mono.just(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(franquiciaService.asociarProductoASucursal(sucursalId, productoId))
                .expectNext(producto)
                .verifyComplete();

        verify(sucursalRepository).findById(sucursalId);
        verify(productoRepository).findById(productoId);
        verify(productoRepository).save(producto);
    }

    @Test
    void getAllProductos_DebeRetornarTodosLosProductos() {
        Producto otroProducto = new Producto();
        otroProducto.setId("producto-456");
        otroProducto.setNombre("Otro Producto");
        otroProducto.setStock(5);

        when(productoRepository.findAll()).thenReturn(Flux.just(producto, otroProducto));

        StepVerifier.create(franquiciaService.getAllProductos())
                .expectNext(producto)
                .expectNext(otroProducto)
                .verifyComplete();

        verify(productoRepository).findAll();
    }

    @Test
    void getProductoById_ConIdValido_DebeRetornarProducto() {
        when(productoRepository.findById(anyString())).thenReturn(Mono.just(producto));

        StepVerifier.create(franquiciaService.getProductoById(productoId))
                .expectNext(producto)
                .verifyComplete();

        verify(productoRepository).findById(productoId);
    }

    @Test
    void getProductoById_ConIdInexistente_DebeLanzarError() {
        when(productoRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.getProductoById(productoId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Producto no encontrado con ID: " + productoId))
                .verify();

        verify(productoRepository).findById(productoId);
    }

    @Test
    void modificarStockProducto_ConDatosValidos_DebeActualizarStock() {
        when(productoRepository.findById(anyString())).thenReturn(Mono.just(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        int nuevoStock = 20;

        StepVerifier.create(franquiciaService.modificarStockProducto(productoId, nuevoStock))
                .assertNext(productoActualizado -> {
                    assertEquals(nuevoStock, productoActualizado.getStock());
                    assertEquals(productoId, productoActualizado.getId());
                })
                .verifyComplete();

        verify(productoRepository).findById(productoId);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void modificarStockProducto_ConStockNegativo_DebeLanzarError() {
        int stockNegativo = -5;

        StepVerifier.create(franquiciaService.modificarStockProducto(productoId, stockNegativo))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El stock no puede ser negativo"))
                .verify();
    }

    @Test
    void eliminarProducto_ConIdValido_DebeEliminarProducto() {
        when(productoRepository.findById(anyString())).thenReturn(Mono.just(producto));
        when(productoRepository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.eliminarProducto(productoId))
                .verifyComplete();

        verify(productoRepository).findById(productoId);
        verify(productoRepository).deleteById(productoId);
    }

    @Test
    void actualizarNombreProducto_ConDatosValidos_DebeActualizarNombre() {
        when(productoRepository.findById(anyString())).thenReturn(Mono.just(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        String nuevoNombre = "Producto Actualizado";

        StepVerifier.create(franquiciaService.actualizarNombreProducto(productoId, nuevoNombre))
                .assertNext(productoActualizado -> {
                    assertEquals(nuevoNombre, productoActualizado.getNombre());
                    assertEquals(productoId, productoActualizado.getId());
                })
                .verifyComplete();

        verify(productoRepository).findById(productoId);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizarNombreProducto_ConNombreVacio_DebeLanzarError() {
        StepVerifier.create(franquiciaService.actualizarNombreProducto(productoId, "  "))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El nuevo nombre no puede estar vacío"))
                .verify();
    }

    @Test
    void productoConMasStockPorSucursal_DebeRetornarProductosConMasStock() {
        Sucursal sucursal1 = new Sucursal();
        sucursal1.setId("suc-1");
        sucursal1.setNombre("Sucursal 1");

        Sucursal sucursal2 = new Sucursal();
        sucursal2.setId("suc-2");
        sucursal2.setNombre("Sucursal 2");

        Producto producto1 = new Producto();
        producto1.setId("prod-1");
        producto1.setNombre("Producto 1");
        producto1.setStock(10);

        Producto producto2 = new Producto();
        producto2.setId("prod-2");
        producto2.setNombre("Producto 2");
        producto2.setStock(20);

        Producto producto3 = new Producto();
        producto3.setId("prod-3");
        producto3.setNombre("Producto 3");
        producto3.setStock(5);

        when(sucursalRepository.findAllByFranquiciaId(anyString())).thenReturn(Flux.just(sucursal1, sucursal2));
        when(productoRepository.findAllBySucursalId("suc-1")).thenReturn(Flux.just(producto1, producto2));
        when(productoRepository.findAllBySucursalId("suc-2")).thenReturn(Flux.just(producto3));

        StepVerifier.create(franquiciaService.productoConMasStockPorSucursal("franquicia-id"))
                .assertNext(resultMap -> {
                    assertEquals("suc-1", resultMap.get("sucursalId"));
                    assertEquals("Sucursal 1", resultMap.get("sucursalNombre"));
                    Producto maxProducto = (Producto) resultMap.get("producto");
                    assertEquals("prod-2", maxProducto.getId());
                    assertEquals(20, maxProducto.getStock());
                })
                .assertNext(resultMap -> {
                    assertEquals("suc-2", resultMap.get("sucursalId"));
                    assertEquals("Sucursal 2", resultMap.get("sucursalNombre"));
                    Producto maxProducto = (Producto) resultMap.get("producto");
                    assertEquals("prod-3", maxProducto.getId());
                    assertEquals(5, maxProducto.getStock());
                })
                .verifyComplete();
    }
    @Test
    void registrarSucursal_ConDatosValidos_DebeGuardarSucursal() {
        when(sucursalRepository.findByNombre(anyString())).thenReturn(Mono.empty());
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(sucursal));

        StepVerifier.create(franquiciaService.registrarSucursal(sucursal))
                .expectNext(sucursal)
                .verifyComplete();

        verify(sucursalRepository).findByNombre(sucursal.getNombre());
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    void asociarSucursalAFranquicia_ConDatosValidos_DebeAsociarSucursal() {
        when(franquiciaRepository.findById(anyString())).thenReturn(Mono.just(franquicia));
        when(sucursalRepository.findById(anyString())).thenReturn(Mono.just(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(sucursal));

        StepVerifier.create(franquiciaService.asociarSucursalAFranquicia(franquiciaId, sucursalId))
                .expectNext(sucursal)
                .verifyComplete();

        verify(franquiciaRepository).findById(franquiciaId);
        verify(sucursalRepository).findById(sucursalId);
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    void getAllSucursales_DebeRetornarTodasLasSucursales() {
        Sucursal otraSucursal = new Sucursal();
        otraSucursal.setId("sucursal-456");
        otraSucursal.setNombre("Otra Sucursal");

        when(sucursalRepository.findAll()).thenReturn(Flux.just(sucursal, otraSucursal));

        StepVerifier.create(franquiciaService.getAllSucursales())
                .expectNext(sucursal)
                .expectNext(otraSucursal)
                .verifyComplete();

        verify(sucursalRepository).findAll();
    }

    @Test
    void getSucursalById_ConIdValido_DebeRetornarSucursal() {
        when(sucursalRepository.findById(anyString())).thenReturn(Mono.just(sucursal));

        StepVerifier.create(franquiciaService.getSucursalById(sucursalId))
                .expectNext(sucursal)
                .verifyComplete();

        verify(sucursalRepository).findById(sucursalId);
    }

    @Test
    void getSucursalById_ConIdInexistente_DebeLanzarError() {
        when(sucursalRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.getSucursalById(sucursalId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Sucursal no encontrada con ID: " + sucursalId))
                .verify();

        verify(sucursalRepository).findById(sucursalId);
    }

    @Test
    void actualizarNombreSucursal_ConDatosValidos_DebeActualizarNombre() {
        when(sucursalRepository.findById(anyString())).thenReturn(Mono.just(sucursal));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(sucursal));

        String nuevoNombre = "Sucursal Actualizada";

        StepVerifier.create(franquiciaService.actualizarNombreSucursal(sucursalId, nuevoNombre))
                .assertNext(sucursalActualizada -> {
                    assertEquals(nuevoNombre, sucursalActualizada.getNombre());
                    assertEquals(sucursalId, sucursalActualizada.getId());
                })
                .verifyComplete();

        verify(sucursalRepository).findById(sucursalId);
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void actualizarNombreSucursal_ConNombreVacio_DebeLanzarError() {
        StepVerifier.create(franquiciaService.actualizarNombreSucursal(sucursalId, "  "))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El nuevo nombre no puede estar vacío"))
                .verify();
    }
    @Test
    void registrarFranquicia_ConDatosValidos_DebeGuardarFranquicia() {
        Franquicia franquicia1 = new Franquicia();
        franquicia1.setNombre("Franquicia Test");

        when(franquiciaRepository.findByNombre(anyString())).thenReturn(Mono.empty());
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia1));

        StepVerifier.create(franquiciaService.registrarFranquicia(franquicia1))
                .expectNext(franquicia1)
                .verifyComplete();

        verify(franquiciaRepository).findByNombre(franquicia1.getNombre());
        verify(franquiciaRepository).save(franquicia1);
    }

    @Test
    void registrarFranquicia_ConNombreVacio_DebeLanzarError() {
        Franquicia franquicia1 = new Franquicia();
        franquicia1.setNombre("");

        StepVerifier.create(franquiciaService.registrarFranquicia(franquicia1))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El nombre de la franquicia no puede estar vacío"))
                .verify();
    }


    @Test
    void actualizarNombreFranquicia_ConDatosValidos_DebeActualizarNombre() {
        Franquicia franquicia1 = new Franquicia();
        franquicia1.setId("franquicia-123");
        franquicia1.setNombre("Franquicia Test");

        when(franquiciaRepository.findById(anyString())).thenReturn(Mono.just(franquicia1));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia1));

        String nuevoNombre = "Franquicia Actualizada";

        StepVerifier.create(franquiciaService.actualizarNombreFranquicia(franquicia1.getId(), nuevoNombre))
                .expectNext(franquicia1)
                .verifyComplete();

        verify(franquiciaRepository).findById(franquicia1.getId());
        verify(franquiciaRepository).save(franquicia1);
    }

    @Test
    void actualizarNombreFranquicia_ConNombreVacio_DebeLanzarError() {
        StepVerifier.create(franquiciaService.actualizarNombreFranquicia("franquicia-123", ""))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("El nuevo nombre no puede estar vacío"))
                .verify();
    }

    @Test
    void actualizarNombreFranquicia_ConIdInexistente_DebeLanzarError() {
        when(franquiciaRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.actualizarNombreFranquicia("franquicia-123", "Nuevo Nombre"))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Franquicia no encontrada con ID: franquicia-123"))
                .verify();

        verify(franquiciaRepository).findById("franquicia-123");
    }
    @Test
    void getFranquiciaCompleta_ConIdValido_DebeRetornarFranquiciaCompleta() {
        Franquicia franquicia1 = new Franquicia();
        franquicia1.setId("franquicia-123");
        franquicia.setNombre("Franquicia Test");

        Sucursal sucursal1 = new Sucursal();
        sucursal1.setId("sucursal-123");
        sucursal1.setNombre("Sucursal Test");

        Producto producto1 = new Producto();
        producto1.setId("producto-123");
        producto1.setNombre("Producto Test");

        when(franquiciaRepository.findById(anyString())).thenReturn(Mono.just(franquicia1));
        when(sucursalRepository.findAllByFranquiciaId(anyString())).thenReturn(Flux.just(sucursal1));
        when(productoRepository.findAllBySucursalId(anyString())).thenReturn(Flux.just(producto1));

        StepVerifier.create(franquiciaService.getFranquiciaCompleta(franquicia1.getId()))
                .assertNext(franquiciaDetailsDTO -> {
                    assertEquals(franquicia1.getId(), franquiciaDetailsDTO.getId());
                    assertEquals(franquicia1.getNombre(), franquiciaDetailsDTO.getNombre());
                    assertEquals(1, franquiciaDetailsDTO.getSucursales().size());
                    assertEquals(sucursal1.getId(), franquiciaDetailsDTO.getSucursales().get(0).getId());
                    assertEquals(1, franquiciaDetailsDTO.getSucursales().get(0).getProductos().size());
                    assertEquals(producto1.getId(), franquiciaDetailsDTO.getSucursales().get(0).getProductos().get(0).getId());
                })
                .verifyComplete();

        verify(franquiciaRepository).findById(franquicia1.getId());
        verify(sucursalRepository).findAllByFranquiciaId(franquicia1.getId());
        verify(productoRepository).findAllBySucursalId(sucursal1.getId());
    }

    @Test
    void getFranquiciaCompleta_ConIdInexistente_DebeLanzarError() {
        when(franquiciaRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.getFranquiciaCompleta("franquicia-123"))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Franquicia no encontrada con ID: franquicia-123"))
                .verify();

        verify(franquiciaRepository).findById("franquicia-123");
    }
}
