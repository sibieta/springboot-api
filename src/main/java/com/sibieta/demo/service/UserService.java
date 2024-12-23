package com.sibieta.demo.service;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sibieta.demo.config.JwtUtils;
import com.sibieta.demo.model.User;
import com.sibieta.demo.model.dto.UserDTO;
import com.sibieta.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    public UserDTO addUser(User user) {

        if (!isValidEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no es válido.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya registrado");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña no cumple con el estandar.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());

        user.setToken(jwtUtils.generateToken(user));

        user.setActive(true);

        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }

    public UserDTO getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        return new UserDTO(user);
    }

    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
