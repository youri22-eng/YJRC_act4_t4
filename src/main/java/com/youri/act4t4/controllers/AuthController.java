package com.youri.act4t4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youri.act4t4.dto.AuthResponse;
import com.youri.act4t4.dto.LoginRequest;
import com.youri.act4t4.dto.RegisterRequest;
import com.youri.act4t4.dto.UsuarioResponse;
import com.youri.act4t4.servicios.AuthServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServicio authServicio;

    // POST /api/auth/register -> 201 Created
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authServicio.registrar(request));
    }

    // POST /api/auth/login -> 200 OK con el token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authServicio.login(request));
    }
}