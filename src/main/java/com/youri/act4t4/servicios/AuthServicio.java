package com.youri.act4t4.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.youri.act4t4.dto.AuthResponse;
import com.youri.act4t4.dto.LoginRequest;
import com.youri.act4t4.dto.RegisterRequest;
import com.youri.act4t4.dto.UsuarioResponse;
import com.youri.act4t4.excepciones.ConflictoException;
import com.youri.act4t4.models.Usuario;
import com.youri.act4t4.repositorio.UsuarioRepositorio;
import com.youri.act4t4.seguridad.JwtServicio;

@Service
public class AuthServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtServicio jwtServicio;

    public UsuarioResponse registrar(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (usuarioRepo.existsByEmail(email)) {
            throw new ConflictoException("Ya existe un usuario registrado con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre().trim());
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(request.password()));

        Usuario guardado = usuarioRepo.save(usuario);
        return new UsuarioResponse(guardado.getId(), guardado.getNombre(), guardado.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        // 1. Busca al usuario por email
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));

        // 2. Compara la contrasena escrita con el hash BCrypt guardado
        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        // 3. Si todo coincide, genera el token JWT
        String token = jwtServicio.generarToken(email);
        return new AuthResponse(token, "Bearer", usuario.getNombre(), usuario.getEmail());
    }
}