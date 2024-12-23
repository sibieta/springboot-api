package com.sibieta.demo.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sibieta.demo.model.User;
import com.sibieta.demo.model.dto.UserDTO;
import com.sibieta.demo.service.UserService;

@Controller
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;


	public UserController(UserService userService) {
        this.userService = userService;
    }

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		UserDTO userDTO = userService.getUser(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        UserDTO savedUser = userService.addUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }



}