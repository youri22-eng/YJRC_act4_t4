package com.youri.act4t4.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.youri.act4t4.dto.CategoriaResponse;
import com.youri.act4t4.dto.PaginaResponse;
import com.youri.act4t4.dto.ProductoRequest;
import com.youri.act4t4.dto.ProductoResponse;
import com.youri.act4t4.excepciones.RecursoNoEncontradoException;
import com.youri.act4t4.models.Categoria;
import com.youri.act4t4.models.Producto;
import com.youri.act4t4.repositorio.CategoriaRepositorio;
import com.youri.act4t4.repositorio.ProductoRepositorio;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepo;

    @Autowired
    private CategoriaRepositorio categoriaRepo;

    // LISTAR con paginacion
    public PaginaResponse<ProductoResponse> listar(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("El parametro page no puede ser negativo");
        }
        if (size < 1 || size > 50) {
            throw new IllegalArgumentException("El parametro size debe estar entre 1 y 50");
        }

        Page<Producto> pagina = productoRepo.findAll(PageRequest.of(page, size, Sort.by("id")));
        List<ProductoResponse> contenido = pagina.getContent().stream().map(this::aRespuesta).toList();

        return new PaginaResponse<>(contenido, pagina.getNumber(), pagina.getSize(),
                pagina.getTotalElements(), pagina.getTotalPages());
    }

    // VER UNO
    public ProductoResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    // CREAR
    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto();
        copiarDatos(producto, request);
        return aRespuesta(productoRepo.save(producto));
    }

    // ACTUALIZAR
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscar(id);
        copiarDatos(producto, request);
        return aRespuesta(productoRepo.save(producto));
    }

    // ELIMINAR
    public void eliminar(Long id) {
        productoRepo.delete(buscar(id));
    }

    public List<CategoriaResponse> listarCategorias() {
        return categoriaRepo.findAll().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre()))
                .toList();
    }

    private Producto buscar(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con id " + id + " no encontrado"));
    }

    private void copiarDatos(Producto producto, ProductoRequest request) {
        Categoria categoria = categoriaRepo.findById(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La categoria con id " + request.categoriaId() + " no existe"));

        producto.setNombre(request.nombre().trim());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoria(categoria);
    }

    // Entity -> DTO de respuesta
    private ProductoResponse aRespuesta(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getPrecio(), p.getStock(),
                p.getCategoria().getId(), p.getCategoria().getNombre());
    }
}