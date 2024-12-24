package com.sibieta.demo;

import com.sibieta.demo.model.Usuario;
import com.sibieta.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByEmail_existingUser() {
        // Create and save a user
        Usuario user = new Usuario();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        usuarioRepository.save(user);

        Optional<Usuario> foundUser = usuarioRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());

    }

    @Test
    void testFindByEmail_nonExistingUser() {

        Optional<Usuario> foundUser = usuarioRepository.findByEmail("nonexistent@example.com");
        assertTrue(foundUser.isEmpty());

    }


    @Test
    void testSaveUser() {
        Usuario user = new Usuario();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");

        Usuario savedUser = usuarioRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());

    }



    @Test
    void testFindById_existingUser() {
        Usuario user = new Usuario();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        usuarioRepository.save(user);

        UUID generatedId = user.getId();
        Optional<Usuario> foundUser = usuarioRepository.findById(generatedId);

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
    }

    @Test
    void testFindById_nonExistingUser() {
         UUID uuid = UUID.randomUUID();
        Optional<Usuario> foundUser = usuarioRepository.findById(uuid);
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void testExistsByEmail_existingUser() {
        Usuario user = new Usuario();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        usuarioRepository.save(user);


        assertTrue(usuarioRepository.existsByEmail("test@example.com"));
    }


    @Test
    void testExistsByEmail_nonExistingUser() {

        assertFalse(usuarioRepository.existsByEmail("nonexistent@example.com"));
    }

}
