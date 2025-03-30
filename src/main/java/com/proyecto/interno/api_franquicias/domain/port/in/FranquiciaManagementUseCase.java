package com.proyecto.interno.api_franquicias.domain.port.in;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

public interface FranquiciaManagementUseCase {

    // Operaciones de Franquicia
    Mono<Franquicia> registrarFranquicia(Franquicia franquicia);
    Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre);

    // Operaciones de Sucursal
    Mono<Sucursal> agregarSucursal(String franquiciaId, Sucursal sucursal);
    Mono<Sucursal> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre);

    // Operaciones de Producto
    Mono<Producto> agregarProducto(String sucursalId, Producto producto);
    Mono<Void> eliminarProducto(String productoId);
    Mono<Producto> actualizarNombreProducto(String productoId, String nuevoNombre);
    Mono<Producto> modificarStockProducto(String productoId, int nuevoStock);

    // Consulta: Producto con mayor stock por sucursal para una franquicia
    Flux<Map<String, Object>> productoConMasStockPorSucursal(String franquiciaId);
}
