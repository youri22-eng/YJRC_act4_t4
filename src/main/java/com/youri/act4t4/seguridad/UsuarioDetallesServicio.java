package com.youri.act4t4.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.youri.act4t4.models.Usuario;
import com.youri.act4t4.repositorio.UsuarioRepositorio;

@Service
public class UsuarioDetallesServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}