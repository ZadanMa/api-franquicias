package com.proyecto.interno.api_franquicias.domain.port.in;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

public interface FranquiciaManagementUseCase {

    Mono<Franquicia> registrarFranquicia(Franquicia franquicia);
    Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre);

    Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal sucursal);
    Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre);

    Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto producto);
    Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalId, String productoId);
    Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre);
    Mono<Franquicia> modificarStockProducto(String franquiciaId, String sucursalId, String productoId, int nuevoStock);

    Flux<Map<String, Object>> productoConMasStockPorSucursal(String franquiciaId);
}
