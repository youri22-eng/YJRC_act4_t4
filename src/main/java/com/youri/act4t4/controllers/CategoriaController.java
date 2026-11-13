package com.youri.act4t4.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youri.act4t4.dto.CategoriaResponse;
import com.youri.act4t4.servicios.ProductoServicio;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ProductoServicio servicio;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(servicio.listarCategorias());
    }
}