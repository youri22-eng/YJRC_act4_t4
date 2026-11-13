package com.youri.act4t4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youri.act4t4.dto.PaginaResponse;
import com.youri.act4t4.dto.ProductoRequest;
import com.youri.act4t4.dto.ProductoResponse;
import com.youri.act4t4.servicios.ProductoServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoServicio servicio;

    // GET /api/productos?page=0&size=5 -> 200 OK (paginado)
    @GetMapping
    public ResponseEntity<PaginaResponse<ProductoResponse>> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(servicio.listar(page, size));
    }

    // GET /api/productos/1 -> 200 OK o 404
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtener(@PathVariable("id") Long id) {
        return ResponseEntity.ok(servicio.obtener(id));
    }

    // POST /api/productos -> 201 Created (o 400 si los datos son invalidos)
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(request));
    }

    // PUT /api/productos/1 -> 200 OK, 400 o 404
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable("id") Long id,
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(servicio.actualizar(id, request));
    }

    // DELETE /api/productos/1 -> 204 No Content o 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}