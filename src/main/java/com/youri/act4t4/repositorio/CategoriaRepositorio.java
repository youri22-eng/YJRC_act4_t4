package com.youri.act4t4.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youri.act4t4.models.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
}