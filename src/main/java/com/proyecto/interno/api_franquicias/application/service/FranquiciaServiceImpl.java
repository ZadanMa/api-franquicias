package com.proyecto.interno.api_franquicias.application.service;

import com.proyecto.interno.api_franquicias.application.dto.FranquiciaDetailsDTO;
import com.proyecto.interno.api_franquicias.application.dto.SucursalDetailsDTO;
import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.model.Producto;

import com.proyecto.interno.api_franquicias.domain.port.in.FranquiciaManagementUseCase;
import com.proyecto.interno.api_franquicias.domain.port.out.FranquiciaRepository;
import com.proyecto.interno.api_franquicias.domain.port.out.SucursalRepository;
import com.proyecto.interno.api_franquicias.domain.port.out.ProductoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FranquiciaServiceImpl implements FranquiciaManagementUseCase {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public FranquiciaServiceImpl(FranquiciaRepository franquiciaRepository,
                                          SucursalRepository sucursalRepository,
                                          ProductoRepository productoRepository) {
        this.franquiciaRepository = franquiciaRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Mono<Franquicia> registrarFranquicia(Franquicia franquicia) {
        return Mono.just(franquicia)
                .filter(f -> f.getNombre() != null && !f.getNombre().isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nombre de la franquicia no puede estar vacío")))
                .flatMap(franquiciaRepository::save);
    }
    @Override
    public Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre) {
        return Mono.just(nuevoNombre)
                .filter(nombre -> nombre != null && !nombre.isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nuevo nombre no puede estar vacío")))
                .flatMap(nombre -> franquiciaRepository.findById(franquiciaId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada con ID: " + franquiciaId)))
                        .flatMap(franquicia -> {
                            franquicia.setNombre(nombre);
                            return franquiciaRepository.save(franquicia);
                        }));
    }
    @Override
    public Flux<Map<String, Object>> productoConMasStockPorSucursal(String franquiciaId) {
        return sucursalRepository.findAllByFranquiciaId(franquiciaId)
                .flatMap(sucursal ->
                        productoRepository.findAllBySucursalId(sucursal.getId())
                                .reduce((p1, p2) -> p1.getStock() >= p2.getStock() ? p1 : p2)
                                .map(maxProducto -> {
                                    Map<String, Object> result = new LinkedHashMap<>();
                                    result.put("sucursalId", sucursal.getId());
                                    result.put("sucursalNombre", sucursal.getNombre());
                                    result.put("producto", maxProducto);
                                    return result;
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    Map<String, Object> emptyResult = new LinkedHashMap<>();
                                    emptyResult.put("sucursalId", sucursal.getId());
                                    emptyResult.put("sucursalNombre", sucursal.getNombre());
                                    emptyResult.put("producto", null);
                                    return Mono.just(emptyResult);
                                }))
                );
    }
    @Override
    public Mono<FranquiciaDetailsDTO> getFranquiciaCompleta(String franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada con ID: " + franquiciaId)))
                .flatMap(franquicia -> {
                    FranquiciaDetailsDTO franquiciaDTO = new FranquiciaDetailsDTO();
                    franquiciaDTO.setId(franquicia.getId());
                    franquiciaDTO.setNombre(franquicia.getNombre());
                    franquiciaDTO.setSucursales(new ArrayList<>());

                    return sucursalRepository.findAllByFranquiciaId(franquiciaId)
                            .flatMap(sucursal -> {
                                SucursalDetailsDTO sucursalDTO = new SucursalDetailsDTO();
                                sucursalDTO.setId(sucursal.getId());
                                sucursalDTO.setNombre(sucursal.getNombre());
                                return productoRepository.findAllBySucursalId(sucursal.getId())
                                        .reduce(
                                                new ArrayList<Producto>(),
                                                (list, producto) -> {
                                                    list.add(producto);
                                                    return list;
                                                }
                                        )
                                        .map(productos -> {
                                            sucursalDTO.setProductos(productos);
                                            return sucursalDTO;
                                        });
                            })
                            .reduce(
                                    franquiciaDTO,
                                    (dto, sucursalDTO) -> {
                                        dto.getSucursales().add(sucursalDTO);
                                        return dto;
                                    }
                            );
                });
    }


    @Override
    public Mono<Sucursal> agregarSucursal(String franquiciaId, Sucursal sucursal) {
        return Mono.just(sucursal)
                .filter(s -> s != null)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La sucursal no puede ser null")))
                .filter(s -> s.getNombre() != null && !s.getNombre().isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nombre de la sucursal no puede estar vacío")))
                .flatMap(s -> franquiciaRepository.findById(franquiciaId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada con ID: " + franquiciaId)))
                        .map(franquicia -> {
                            s.setFranquiciaId(franquicia.getId());
                            return s;
                        })
                        .flatMap(sucursalRepository::save));
    }
    @Override
    public Mono<Sucursal> registrarSucursal(Sucursal sucursal) {
        return Mono.just(sucursal)
                .filter(s -> s != null)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La sucursal no puede ser null")))
                .filter(s -> s.getNombre() != null && !s.getNombre().isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nombre de la sucursal no puede estar vacío")))
                .flatMap(sucursalRepository::save);
    }
    @Override
    public Mono<Sucursal> asociarSucursalAFranquicia(String franquiciaId, String sucursalId) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
                .then(sucursalRepository.findById(sucursalId))
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada")))
                .flatMap(sucursal -> {
                    sucursal.setFranquiciaId(franquiciaId);
                    return sucursalRepository.save(sucursal);
                });
    }

    @Override
    public Flux<Sucursal> getAllSucursales() {
        return sucursalRepository.findAll()
                .doOnNext(sucursal -> System.out.println("🟢 [DEBUG] Sucursal encontrada: " + sucursal.getNombre()))
                .switchIfEmpty(Flux.error(new RuntimeException("No hay sucursales en la BD"))); // ← Forzar error si está vacío
    }
    @Override
    public Mono<Sucursal> getSucursalById(String sucursalId) {
        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada con ID: " + sucursalId)));
    }


    @Override
    public Mono<Producto> agregarProducto(String sucursalId, Producto producto) {
        return Mono.just(producto)
                .filter(p -> p != null)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El producto no puede ser null")))
                .filter(p -> p.getNombre() != null && !p.getNombre().isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nombre del producto es requerido")))
                .flatMap(p -> sucursalRepository.findById(sucursalId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada con ID: " + sucursalId)))
                        .map(sucursal -> {
                            p.setSucursalId(sucursal.getId());
                            return p;
                        })
                        .flatMap(productoRepository::save));
    }
    @Override
    public Mono<Producto> registrarProducto(Producto producto) {
        return Mono.just(producto)
                .filter(p -> p != null)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El producto no puede ser null")))
                .filter(p -> p.getNombre() != null && !p.getNombre().isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nombre del producto es requerido")))
                .flatMap(productoRepository::save);
    }
    @Override
    public Mono<Producto> asociarProductoASucursal(String sucursalId, String productoId) {
        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada con ID: " + sucursalId)))
                .then(productoRepository.findById(productoId))
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado con ID: " + productoId)))
                .flatMap(producto -> {
                    producto.setSucursalId(sucursalId);
                    return productoRepository.save(producto);
                });
    }
    @Override
    public Flux<Producto> getAllProductos() {
        return productoRepository.findAll();
    }
    @Override
    public Mono<Producto> getProductoById(String productoId) {
        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado con ID: " + productoId)));
    }
    @Override
    public Mono<Producto> modificarStockProducto(String productoId, int nuevoStock) {
        return Mono.just(nuevoStock)
                .filter(stock -> stock >= 0)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El stock no puede ser negativo")))
                .flatMap(stock -> productoRepository.findById(productoId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado con ID: " + productoId)))
                        .flatMap(producto -> {
                            producto.setStock(stock);
                            return productoRepository.save(producto);
                        }));
    }
    @Override
    public Mono<Void> eliminarProducto(String sucursalId, String productoId) {
        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada con ID: " + sucursalId)))
                .then(productoRepository.findById(productoId))
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado con ID: " + productoId)))
                .filter(producto -> producto.getSucursalId().equals(sucursalId))
                .switchIfEmpty(Mono.error(new RuntimeException("El producto no pertenece a la sucursal indicada")))
                .flatMap(producto -> productoRepository.deleteById(productoId));
    }
    @Override
    public Mono<Sucursal> actualizarNombreSucursal(String sucursalId, String nuevoNombre) {
        if(nuevoNombre == null || nuevoNombre.isEmpty()){
            return Mono.error(new IllegalArgumentException("El nuevo nombre no puede estar vacío"));
        }
        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada con ID: " + sucursalId)))
                .flatMap(sucursal -> {
                    sucursal.setNombre(nuevoNombre);
                    return sucursalRepository.save(sucursal);
                });
    }

    @Override
    public Mono<Producto> actualizarNombreProducto(String productoId, String nuevoNombre) {
        return Mono.justOrEmpty(nuevoNombre)
                .filter(nombre -> !nombre.trim().isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nuevo nombre no puede estar vacío")))
                .flatMap(nombre -> productoRepository.findById(productoId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado con ID: " + productoId)))
                        .flatMap(producto -> {
                            producto.setNombre(nombre);
                            return productoRepository.save(producto);
                        }));
    }

}
