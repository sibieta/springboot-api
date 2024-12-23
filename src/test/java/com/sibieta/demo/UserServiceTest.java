package com.sibieta.demo;

import com.sibieta.demo.config.JwtUtils;
import com.sibieta.demo.model.User;
import com.sibieta.demo.model.dto.UserDTO;
import com.sibieta.demo.repository.UserRepository;
import com.sibieta.demo.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserService userService;

    @Test
    void testAddUser_validUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("Test1234@");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        when(jwtUtils.generateToken(any())).thenReturn("testToken");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            savedUser.setCreated(new Date());
            savedUser.setModified(new Date());
            savedUser.setLastLogin(new Date());
            return savedUser;
        });

        UserDTO savedUserDTO = userService.addUser(user);

        assertNotNull(savedUserDTO);
        assertEquals(1L, savedUserDTO.getId());
        assertEquals("John Doe", savedUserDTO.getName());
        assertEquals("john.doe@example.com", savedUserDTO.getEmail());

    }

    @Test
    void testAddUser_duplicateEmail() {
        User user = new User();
        user.setEmail("duplicate@example.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.addUser(user));
    }

    @Test
    void testAddUser_invalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");

        assertThrows(ResponseStatusException.class, () -> userService.addUser(user));
    }

    @Test
    void testAddUser_invalidPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("invalid");

        lenient().when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> userService.addUser(user));

    }

    @Test
    void testGetUser_existingUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUser(1L);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());

    }

    @Test
    void testGetUser_nonExistingUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUser(99L));

    }

}
