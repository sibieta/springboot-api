package com.sibieta.demo;

import com.sibieta.demo.config.JwtUtils;
import com.sibieta.demo.model.Usuario;
import com.sibieta.demo.model.dto.UsuarioCreationResponseDTO;
import com.sibieta.demo.model.dto.UsuarioDTO;
import com.sibieta.demo.repository.UsuarioRepository;
import com.sibieta.demo.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UsuarioRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void testInjections() {
        assertNotNull(userService);
        assertNotNull(userRepository);
        assertNotNull(userService.getEmailRegex());
        assertNotNull(userService.getPasswordRegex());
    }

    @Test
    void testAddUser_validUser() {
        Usuario user = new Usuario();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("Test1234@");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        when(jwtUtils.generateToken(any())).thenReturn("testToken");

        when(userRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            savedUser.setCreated(new Date());
            savedUser.setModified(new Date());
            savedUser.setLastLogin(new Date());
            return savedUser;
        });

        UsuarioCreationResponseDTO savedUserDTO = userService.addUser(user);

        assertNotNull(savedUserDTO);
        assertEquals(1L, savedUserDTO.getId());

    }

    @Test
    void testAddUser_duplicateEmail() {
        Usuario user = new Usuario();
        user.setEmail("duplicate@example.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.addUser(user));
    }

    @Test
    void testAddUser_invalidEmail() {
        Usuario user = new Usuario();
        user.setEmail("invalid-email");

        assertThrows(ResponseStatusException.class, () -> userService.addUser(user));
    }

    @Test
    void testAddUser_invalidPassword() {
        Usuario user = new Usuario();
        user.setEmail("test@example.com");
        user.setPassword("invalid");

        lenient().when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> userService.addUser(user));

    }

    @Test
    void testGetUser_existingUser() {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UsuarioDTO userDTO = userService.getUser(1L);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());

    }

    @Test
    void testGetUser_nonExistingUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUser(99L));

    }

}
