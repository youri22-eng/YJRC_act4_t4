package com.youri.act4t4.seguridad;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    @Autowired
    private JwtServicio jwtServicio;

    @Autowired
    private UsuarioDetallesServicio detallesServicio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Busca el header:  Authorization: Bearer <token>
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                // 2. Valida el token y obtiene el email
                String email = jwtServicio.extraerEmail(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 3. Carga el usuario y lo marca como autenticado
                    UserDetails usuario = detallesServicio.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
                // Token invalido, vencido o usuario inexistente: no se autentica.
                // Mas adelante Spring Security respondera 401.
            }
        }

        // 4. Continua con la peticion
        filterChain.doFilter(request, response);
    }
}