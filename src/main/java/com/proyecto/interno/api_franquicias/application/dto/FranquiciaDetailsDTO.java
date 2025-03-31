package com.proyecto.interno.api_franquicias.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class FranquiciaDetailsDTO {
    private String id;
    private String nombre;
    private List<SucursalDetailsDTO> sucursales;
}
