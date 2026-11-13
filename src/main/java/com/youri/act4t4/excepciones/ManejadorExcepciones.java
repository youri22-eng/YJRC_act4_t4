package com.youri.act4t4.excepciones;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ManejadorExcepciones {

    // 400: los datos de entrada no pasan las validaciones (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return respuesta(HttpStatus.BAD_REQUEST, "Datos de entrada invalidos", errores);
    }

    // 400: el JSON esta mal escrito
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la peticion no es un JSON valido", null);
    }

    // 400: un parametro tiene un tipo incorrecto (ej. /api/productos/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "El parametro '" + ex.getName() + "' tiene un valor invalido", null);
    }

    // 400: regla de negocio incumplida (ej. la categoria no existe)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> solicitudInvalida(IllegalArgumentException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    // 404: el recurso no existe
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(RecursoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // 409: conflicto (ej. email ya registrado)
    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<Map<String, Object>> conflicto(ConflictoException ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // 401: email o contrasena incorrectos en el login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> credenciales(BadCredentialsException ex) {
        return respuesta(HttpStatus.UNAUTHORIZED, "Email o contrasena incorrectos", null);
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado, String mensaje, Object errores) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", estado.value());
        cuerpo.put("error", estado.getReasonPhrase());
        cuerpo.put("mensaje", mensaje);
        if (errores != null) {
            cuerpo.put("errores", errores);
        }
        return ResponseEntity.status(estado).body(cuerpo);
    }
}