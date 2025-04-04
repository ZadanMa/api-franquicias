package com.proyecto.interno.api_franquicias.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class FranquiciaDetails {
    private String id;
    private String nombre;
    private List<SucursalDetails> sucursales;
}
