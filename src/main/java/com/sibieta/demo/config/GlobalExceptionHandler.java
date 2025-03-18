package com.sibieta.demo.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST || ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            String errorMessage = ex.getReason();
            errorResponse.put("mensaje", errorMessage);
        } else if(ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            errorResponse.put("mensaje", "No autorizado");
        }else {
            errorResponse.put("mensaje", "Error interno del servidor");
        }
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", "Formato invalido de Id");
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("mensaje","Error interno del servidor: " + ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
