package com.youri.act4t4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotNull(message = "El precio es obligatorio") @PositiveOrZero(message = "El precio no puede ser negativo") Double precio,
        @NotNull(message = "El stock es obligatorio") @PositiveOrZero(message = "El stock no puede ser negativo") Integer stock,
        @NotNull(message = "La categoriaId es obligatoria") Long categoriaId) {
}