package com.youri.act4t4.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youri.act4t4.models.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}