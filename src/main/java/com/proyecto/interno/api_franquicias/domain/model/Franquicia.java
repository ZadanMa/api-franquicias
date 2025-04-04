    package com.proyecto.interno.api_franquicias.domain.model;

    import com.proyecto.interno.api_franquicias.domain.validator.NoNumbers;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Document(collection = "franquicias")
    public class Franquicia {
        @Id
        private String id;
        @NoNumbers
        private String nombre;
    }
