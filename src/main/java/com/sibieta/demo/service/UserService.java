package com.sibieta.demo.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sibieta.demo.config.JwtUtils;
import com.sibieta.demo.model.Usuario;
import com.sibieta.demo.model.dto.UsuarioCreationResponseDTO;
import com.sibieta.demo.model.dto.UsuarioDTO;
import com.sibieta.demo.repository.UsuarioRepository;

import java.util.UUID;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${validation.email.regex}")
    private String emailRegex;

    @Value("${validation.password.regex}")
    private String passwordRegex;

    @Value("${validation.uuid.regex}")
    private String uuidRegex;

    public String getEmailRegex() {
        return emailRegex;
    }

    public String getPasswordRegex() {
        return passwordRegex;
    }

    public String getUuidRegex() {
        return uuidRegex;
    }

    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioCreationResponseDTO addUser(Usuario user) {

        if (!isValidEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no es válido.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya fue registrado");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña no cumple con el estandar.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());

        user.setActive(true);

        Usuario savedUser = userRepository.save(user);
        
        //Token JWT no se persiste en la BD
        UsuarioCreationResponseDTO userDto = new UsuarioCreationResponseDTO(savedUser);
        userDto.setToken(jwtUtils.generateToken(user));

        return userDto;
    
    }

    public UsuarioDTO getUser(UUID userId, String jwt) {

        if(!jwtUtils.validateToken(jwt)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }

        Optional<Usuario> usuarioOptional = userRepository.findById(userId);

        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no existe");
        }
        if (!isValidUUID(userId.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato invalido de Id");
        }

        Usuario user = usuarioOptional.get();

        return new UsuarioDTO(user);
    }

    public List<UsuarioDTO> getUsers(String jwt) {
        if(!jwtUtils.validateToken(jwt)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
        return userRepository.findAll().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    private boolean isValidPassword(String password) {
        return password.matches(passwordRegex);
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidUUID(String uuid) {
        Pattern pattern = Pattern.compile(uuidRegex);
        Matcher matcher = pattern.matcher(uuid);
        return matcher.matches();
    }

}
