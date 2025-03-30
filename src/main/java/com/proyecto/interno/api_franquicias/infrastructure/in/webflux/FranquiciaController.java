package com.proyecto.interno.api_franquicias.infrastructure.in.webflux;

import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import com.proyecto.interno.api_franquicias.domain.port.in.FranquiciaManagementUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final FranquiciaManagementUseCase service;

    @Autowired
    public FranquiciaController(FranquiciaManagementUseCase service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> registrarFranquicia(@RequestBody Franquicia franquicia) {
        return service.registrarFranquicia(franquicia);
    }

    @PostMapping("/{franquiciaId}/sucursales")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Sucursal> agregarSucursal(@PathVariable String franquiciaId,
                                          @RequestBody Sucursal sucursal) {
        return service.agregarSucursal(franquiciaId, sucursal);
    }

    @PostMapping("/sucursales/{sucursalId}/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> agregarProducto(@PathVariable String sucursalId,
                                          @RequestBody Producto producto) {
        return service.agregarProducto(sucursalId, producto);
    }

    @DeleteMapping("/productos/{productoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarProducto(@PathVariable String productoId) {
        return service.eliminarProducto(productoId);
    }

    @PutMapping("/productos/{productoId}/stock")
    public Mono<Producto> modificarStockProducto(@PathVariable String productoId,
                                                 @RequestParam int nuevoStock) {
        return service.modificarStockProducto(productoId, nuevoStock);
    }

    @GetMapping("/{franquiciaId}/productos/mas-stock")
    public Flux<Map<String, Object>> productoConMasStockPorSucursal(@PathVariable String franquiciaId) {
        return service.productoConMasStockPorSucursal(franquiciaId);
    }

}
