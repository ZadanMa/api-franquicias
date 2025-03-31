package com.proyecto.interno.api_franquicias.application.dto;


public class ProductoStockDto {
    private int nuevoStock;

    public ProductoStockDto() {}

    public ProductoStockDto(int nuevoStock) {
        this.nuevoStock = nuevoStock;
    }

    public int getNuevoStock() {
        return nuevoStock;
    }

    public void setNuevoStock(int nuevoStock) {
        this.nuevoStock = nuevoStock;
    }
}
