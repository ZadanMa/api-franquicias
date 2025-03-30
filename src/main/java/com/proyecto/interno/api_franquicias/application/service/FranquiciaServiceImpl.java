package com.proyecto.interno.api_franquicias.application.service;

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
    public Mono<Sucursal> agregarSucursal(String franquiciaId, Sucursal sucursal) {
        return Mono.just(sucursal)
                .filter(s -> s != null)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La sucursal no puede ser null")))
                .flatMap(s -> franquiciaRepository.findById(franquiciaId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada con ID: " + franquiciaId)))
                        .map(franquicia -> {
                            s.setFranquiciaId(franquicia.getId());
                            return s;
                        })
                        .flatMap(sucursalRepository::save));
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
    public Mono<Void> eliminarProducto(String productoId) {
        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado con ID: " + productoId)))
                .flatMap(productoRepository::delete);
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
    public Mono<Sucursal> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        return Mono.just(nuevoNombre)
                .filter(nombre -> nombre != null && !nombre.isEmpty())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El nuevo nombre no puede estar vacío")))
                .flatMap(nombre -> sucursalRepository.findById(sucursalId)
                        .filter(sucursal -> sucursal.getFranquiciaId().equals(franquiciaId))
                        .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada con ID: " + sucursalId + " para la franquicia con ID: " + franquiciaId)))
                        .flatMap(sucursal -> {
                            sucursal.setNombre(nombre);
                            return sucursalRepository.save(sucursal);
                        }));
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
