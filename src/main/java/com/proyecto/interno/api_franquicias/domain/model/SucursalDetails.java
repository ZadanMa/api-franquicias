package com.proyecto.interno.api_franquicias.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class SucursalDetails {
    private String id;
    private String nombre;
    private List<Producto> productos;
}
