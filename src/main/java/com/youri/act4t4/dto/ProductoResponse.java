package com.youri.act4t4.dto;

public record ProductoResponse(Long id, String nombre, Double precio, Integer stock, Long categoriaId,
        String categoriaNombre) {
}