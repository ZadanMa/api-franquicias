//package com.proyecto.interno.api_franquicias.infrastructure.in.webflux;
//
//import com.proyecto.interno.api_franquicias.application.dto.FranquiciaDetailsDTO;
//import com.proyecto.interno.api_franquicias.application.dto.FranquiciaDto;
//import com.proyecto.interno.api_franquicias.application.dto.ProductoDto;
//import com.proyecto.interno.api_franquicias.application.dto.SucursalDto;
//import com.proyecto.interno.api_franquicias.domain.model.Franquicia;
//import com.proyecto.interno.api_franquicias.domain.model.Sucursal;
//import com.proyecto.interno.api_franquicias.domain.model.Producto;
//import com.proyecto.interno.api_franquicias.domain.port.in.FranquiciaManagementUseCase;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/franquicias")
//@Tag(name = "Franquicias", description = "API para gestión de franquicias, y documentación adicional sobre las sucursales y productos")
//public class FranquiciaController {
//
//    private final FranquiciaManagementUseCase service;
//
//    @Autowired
//    public FranquiciaController(FranquiciaManagementUseCase service) {
//        this.service = service;
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Franquicia> registrarFranquicia(@RequestBody Franquicia franquicia) {
//        return service.registrarFranquicia(franquicia);
//    }
//    @PutMapping("/{franquiciaId}")
//    public Mono<Franquicia> actualizarNombreFranquicia(@PathVariable String franquiciaId,
//                                                       @RequestBody FranquiciaDto nuevoFranquicia) {
//        return service.actualizarNombreFranquicia(franquiciaId, nuevoFranquicia.getNuevoFranquicia());
//    }
//    @GetMapping("/{franquiciaId}")
//    public Mono<FranquiciaDetailsDTO> getFranquiciaCompleta(@PathVariable String franquiciaId) {
//        return service.getFranquiciaCompleta(franquiciaId);
//    }
//    @GetMapping("/{franquiciaId}/mas-stock")
//    public Flux<Map<String, Object>> productoConMasStockPorSucursal(@PathVariable String franquiciaId) {
//        return service.productoConMasStockPorSucursal(franquiciaId);
//    }
//
//
//    @PostMapping("/{franquiciaId}/sucursales")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Sucursal> agregarSucursal(@PathVariable String franquiciaId,
//                                          @RequestBody Sucursal sucursal) {
//        return service.agregarSucursal(franquiciaId, sucursal);
//    }
//    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}")
//    public Mono<Sucursal> actualizarNombreSucursal(@PathVariable String franquiciaId,
//                                                   @PathVariable String sucursalId,
//                                                   @RequestBody SucursalDto nombreSucursal) {
//        return service.actualizarNombreSucursal(franquiciaId, sucursalId, nombreSucursal.getNombreSucursal());
//    }
//    @PostMapping("/sucursales")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Sucursal> registrarSucursal(@RequestBody Sucursal sucursal) {
//        return service.registrarSucursal(sucursal);
//    }
//    @PutMapping("/{franquiciaId}/sucursales/asociar/{sucursalId}")
//    public Mono<Sucursal> asociarSucursalAFranquicia(@PathVariable String franquiciaId,
//                                                     @PathVariable String sucursalId) {
//        return service.asociarSucursalAFranquicia(franquiciaId, sucursalId);
//    }
//    @GetMapping("/sucursales")
//    public Flux<Sucursal> getAllSucursales() {
//        return service.getAllSucursales();
//    }
//    @GetMapping("/sucursales/{sucursalId}")
//    public Mono<Sucursal> getSucursalById(@PathVariable String sucursalId) {
//        return service.getSucursalById(sucursalId);
//    }
//
//
//    @PostMapping("/{sucursalId}/productos")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Producto> agregarProducto(@PathVariable String sucursalId,
//                                          @RequestBody Producto producto) {
//        return service.agregarProducto(sucursalId, producto);
//    }
//    @PostMapping("/productos")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Producto> registrarProducto(@RequestBody Producto producto) {
//        return service.registrarProducto(producto);
//    }
//    @PutMapping("/{sucursalId}/productos/asociar/{productoId}")
//    public Mono<Producto> asociarProductoASucursal(@PathVariable String sucursalId,
//                                                   @PathVariable String productoId) {
//        return service.asociarProductoASucursal(sucursalId, productoId);
//    }
//    @GetMapping("/productos")
//    public Flux<Producto> getAllProductos() {
//        return service.getAllProductos();
//    }
//    @GetMapping("/productos/{productoId}")
//    public Mono<Producto> getProductoById(@PathVariable String productoId) {
//        return service.getProductoById(productoId);
//    }
//    @DeleteMapping("/productos/{productoId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public Mono<Void> eliminarProducto(@PathVariable String productoId) {
//        return service.eliminarProducto(productoId);
//    }
//    @PutMapping("/productos-stock/{productoId}")
//    public Mono<Producto> modificarStockProducto(@PathVariable String productoId,
//                                                 @RequestParam int nuevoStock) {
//        return service.modificarStockProducto(productoId, nuevoStock);
//    }
//    @PutMapping("/productos/{productoId}")
//    public Mono<Producto> actualizarNombreProducto(@PathVariable String productoId,
//                                                   @RequestBody ProductoDto nombreProducto) {
//        return service.actualizarNombreProducto(productoId, nombreProducto.getNombreProducto());
//    }
//
//}
