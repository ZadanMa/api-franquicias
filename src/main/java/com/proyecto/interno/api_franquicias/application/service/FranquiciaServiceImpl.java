package com.proyecto.interno.api_franquicias.application.service;

import com.proyecto.interno.api_franquicias.domain.exception.InvalidFranquiciaException;
import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import com.proyecto.interno.api_franquicias.domain.port.in.FranquiciaManagementUseCase;
import com.proyecto.interno.api_franquicias.domain.port.out.FranquiciaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FranquiciaServiceImpl implements FranquiciaManagementUseCase {

    private final FranquiciaRepository franquiciaRepository;

    @Autowired
    public FranquiciaServiceImpl(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    @Override
    public Mono<Franquicia> registrarFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia);
    }

    @Override
    public Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal sucursal) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMap(f -> {
                    f.getSucursales().add(sucursal);
                    return franquiciaRepository.save(f);
                });
    }

    @Override
    public Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto producto) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMap(f -> {
                    return Flux.fromIterable(f.getSucursales())
                            .filter(s -> s.getId().equals(sucursalId))
                            .next()
                            .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Sucursal no encontrada")))
                            .map(sucursal -> {
                                sucursal.getProductos().add(producto);
                                return f;
                            })
                            .flatMap(franquiciaRepository::save);
                });
    }

    @Override
    public Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMap(f -> {
                    return Flux.fromIterable(f.getSucursales())
                            .filter(s -> s.getId().equals(sucursalId))
                            .next()
                            .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Sucursal no encontrada")))
                            .flatMap(sucursal -> {
                                return Flux.fromIterable(sucursal.getProductos())
                                        .filter(p -> p.getId().equals(productoId))
                                        .hasElements()
                                        .flatMap(existeProducto -> {
                                            if (!existeProducto) {
                                                return Mono.error(new InvalidFranquiciaException("Producto no encontrado"));
                                            }
                                            sucursal.getProductos().removeIf(p -> p.getId().equals(productoId));
                                            return franquiciaRepository.save(f);
                                        });
                            });
                });
    }

    @Override
    public Mono<Franquicia> modificarStockProducto(String franquiciaId, String sucursalId, String productoId, int nuevoStock) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMap(f -> {
                    return Flux.fromIterable(f.getSucursales())
                            .filter(s -> s.getId().equals(sucursalId))
                            .next()
                            .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Sucursal no encontrada")))
                            .flatMap(sucursal -> {
                                return Flux.fromIterable(sucursal.getProductos())
                                        .filter(p -> p.getId().equals(productoId))
                                        .next()
                                        .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Producto no encontrado")))
                                        .map(producto -> {
                                            producto.setStock(nuevoStock);
                                            return f;
                                        })
                                        .flatMap(franquiciaRepository::save);
                            });
                });
    }

    @Override
    public Flux<Map<String, Object>> productoConMasStockPorSucursal(String franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales())
                        .flatMap(sucursal ->
                                Flux.fromIterable(sucursal.getProductos())
                                        .reduce((p1, p2) -> p1.getStock() > p2.getStock() ? p1 : p2)
                                        .map(maxProducto -> {
                                            Map<String, Object> result = new LinkedHashMap<>();
                                            result.put("sucursalId", sucursal.getId());
                                            result.put("sucursalNombre", sucursal.getNombre());
                                            result.put("producto", maxProducto);
                                            return result;
                                        })
                        )
                );
    }

    @Override
    public Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMap(f -> {
                    f.setNombre(nuevoNombre);
                    return franquiciaRepository.save(f);
                });
    }

    @Override
    public Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMap(f -> {
                    return Flux.fromIterable(f.getSucursales())
                            .filter(s -> s.getId().equals(sucursalId))
                            .next()
                            .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Sucursal no encontrada")))
                            .map(sucursal -> {
                                sucursal.setNombre(nuevoNombre);
                                return f;
                            })
                            .flatMap(franquiciaRepository::save);
                });
    }
    @Override
    public Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Franquicia no encontrada")))
                .flatMap(f -> {
                    return Flux.fromIterable(f.getSucursales())
                            .filter(s -> s.getId().equals(sucursalId))
                            .next()
                            .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Sucursal no encontrada")))
                            .flatMap(sucursal -> {
                                return Flux.fromIterable(sucursal.getProductos())
                                        .filter(p -> p.getId().equals(productoId))
                                        .next()
                                        .switchIfEmpty(Mono.error(new InvalidFranquiciaException("Producto no encontrado")))
                                        .map(producto -> {
                                            producto.setNombre(nuevoNombre);
                                            return f;
                                        })
                                        .flatMap(franquiciaRepository::save);
                            });
                });
    }
}
