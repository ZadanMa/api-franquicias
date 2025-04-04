package com.proyecto.interno.api_franquicias.domain.port.in;

import com.proyecto.interno.api_franquicias.domain.model.FranquiciaDetails;
import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

public interface FranquiciaManagementUseCase {

    Mono<Franquicia> registrarFranquicia(Franquicia franquicia);
    Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre);
    Flux<Map<String, Object>> productoConMasStockPorSucursal(String franquiciaId);
    Mono<FranquiciaDetails> getFranquiciaCompleta(String franquiciaId);

    Mono<Sucursal> agregarSucursal(String franquiciaId, Sucursal sucursal);
    Mono<Sucursal> registrarSucursal(Sucursal sucursal);
    Mono<Sucursal> asociarSucursalAFranquicia(String franquiciaId, String sucursalId);
    Flux<Sucursal> getAllSucursales();
    Mono<Sucursal> getSucursalById(String sucursalId);
    Mono<Sucursal> actualizarNombreSucursal(String sucursalId, String nuevoNombre);

    Mono<Producto> agregarProducto(String sucursalId, Producto producto);
    Mono<Producto> registrarProducto(Producto producto);
    Mono<Producto> asociarProductoASucursal(String sucursalId, String productoId);
    Flux<Producto> getAllProductos();
    Mono<Producto> getProductoById(String productoId);
    Mono<Void> eliminarProducto(String productoId);
    Mono<Producto> actualizarNombreProducto(String productoId, String nuevoNombre);
    Mono<Producto> modificarStockProducto(String productoId, int nuevoStock);

}
