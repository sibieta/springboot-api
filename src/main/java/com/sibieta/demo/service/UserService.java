package com.sibieta.demo.service;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sibieta.demo.config.JwtUtils;
import com.sibieta.demo.model.Usuario;
import com.sibieta.demo.model.dto.UsuarioCreationResponseDTO;
import com.sibieta.demo.model.dto.UsuarioDTO;
import com.sibieta.demo.repository.UsuarioRepository;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${validation.email.regex}")
    private String emailRegex;
    
    @Value("${validation.password.regex}")
    private String passwordRegex;

    public UsuarioCreationResponseDTO addUser(Usuario user) {

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

        Usuario savedUser = userRepository.save(user);
        return new UsuarioCreationResponseDTO(savedUser);
    }

    public UsuarioDTO getUser(Long userId) {
        Usuario user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        return new UsuarioDTO(user);
    }

    private boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
