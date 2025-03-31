package com.proyecto.interno.api_franquicias.application.dto;

import com.proyecto.interno.api_franquicias.domain.model.Producto;
import lombok.Data;

import java.util.List;

@Data
public class SucursalDetailsDTO {
    private String id;
    private String nombre;
    private List<Producto> productos;
}
