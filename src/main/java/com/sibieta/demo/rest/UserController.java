package com.sibieta.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sibieta.demo.model.Usuario;
import com.sibieta.demo.model.dto.UsuarioCreationResponseDTO;
import com.sibieta.demo.model.dto.UsuarioDTO;
import com.sibieta.demo.service.UserService;

import org.springframework.http.HttpHeaders;

import java.util.UUID;

@Controller
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<UsuarioDTO> getUser(@PathVariable UUID id) {
		UsuarioDTO userDTO = userService.getUser(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@PostMapping
    public ResponseEntity<UsuarioCreationResponseDTO> addUser(@RequestBody Usuario user) {
        UsuarioCreationResponseDTO savedUser = userService.addUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<UsuarioDTO>> getUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado");
        }
        String token = authorizationHeader.substring(7);
		List<UsuarioDTO> usersDTO = userService.getUsers(token);
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
	}



}