package com.sibieta.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sibieta.demo.model.Usuario;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    
}
