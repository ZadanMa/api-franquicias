package com.proyecto.interno.api_franquicias.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sucursales")
public class Sucursal {
    @Id
    private String id;
    private String franquiciaId;
    private String nombre;
}
