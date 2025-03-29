package com.proyecto.interno.api_franquicias.infrastructure.in.webflux;

import com.proyecto.interno.api_franquicias.application.dto.FranquiciaDto;
import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
import com.proyecto.interno.api_franquicias.domain.model.Producto;
import com.proyecto.interno.api_franquicias.application.service.FranquiciaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final FranquiciaServiceImpl franquiciaService;

    @Autowired
    public FranquiciaController(FranquiciaServiceImpl franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> registrarFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaService.registrarFranquicia(franquicia);
    }

    @PostMapping("/{franquiciaId}/sucursales")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> agregarSucursal(@PathVariable String franquiciaId,
                                            @RequestBody Sucursal sucursal) {
        return franquiciaService.agregarSucursal(franquiciaId, sucursal);
    }

    @PostMapping("/{franquiciaId}/sucursales/{sucursalId}/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franquicia> agregarProducto(@PathVariable String franquiciaId,
                                            @PathVariable String sucursalId,
                                            @RequestBody Producto producto) {
        return franquiciaService.agregarProducto(franquiciaId, sucursalId, producto);
    }

    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Franquicia> eliminarProducto(@PathVariable String franquiciaId,
                                             @PathVariable String sucursalId,
                                             @PathVariable String productoId) {
        return franquiciaService.eliminarProducto(franquiciaId, sucursalId, productoId);
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock")
    public Mono<Franquicia> modificarStockProducto(@PathVariable String franquiciaId,
                                                   @PathVariable String sucursalId,
                                                   @PathVariable String productoId,
                                                   @RequestParam int nuevoStock) {
        return franquiciaService.modificarStockProducto(franquiciaId, sucursalId, productoId, nuevoStock);
    }

    @GetMapping("/{franquiciaId}/productos/mas-stock")
    public Flux<Map<String, Object>> productoConMasStockPorSucursal(@PathVariable String franquiciaId) {
        return franquiciaService.productoConMasStockPorSucursal(franquiciaId);
    }
    @PutMapping("/{franquiciaId}/nombre")
    public Mono<Franquicia> actualizarNombreFranquicia(@PathVariable String franquiciaId,
                                                       @RequestBody FranquiciaDto nuevoNombre) {
        return franquiciaService.actualizarNombreFranquicia(franquiciaId, nuevoNombre.getNuevoFranquicia());
    }

    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/nombre")
    public Mono<Franquicia> actualizarNombreSucursal(@PathVariable String franquiciaId,
                                                     @PathVariable String sucursalId,
                                                     @RequestParam String nuevoNombre) {
        return franquiciaService.actualizarNombreSucursal(franquiciaId, sucursalId, nuevoNombre);
    }
    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre")
    public Mono<Franquicia> actualizarNombreProducto(@PathVariable String franquiciaId,
                                                     @PathVariable String sucursalId,
                                                     @PathVariable String productoId,
                                                     @RequestParam String nuevoNombre) {
        return franquiciaService.actualizarNombreProducto(franquiciaId, sucursalId, productoId, nuevoNombre);
    }
}
