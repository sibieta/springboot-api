package com.sibieta.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibieta.demo.model.Usuario;
import com.sibieta.demo.model.dto.UsuarioCreationResponseDTO;
import com.sibieta.demo.model.dto.UsuarioDTO;
import com.sibieta.demo.rest.UserController;
import com.sibieta.demo.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDetails testUser;

    @BeforeEach
    void setUp() {
        testUser = User.withUsername("demotest")
                .password(passwordEncoder.encode("12345678"))
                .roles("USER")
                .build();

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "demotest", password = "12345678", roles = "USER")
    void testGetUserById_validId() throws Exception {
        UsuarioDTO userDTO = new UsuarioDTO();
        userDTO.setId(1L);
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setPhones(null);
        userDTO.setCreated(new Date());
        userDTO.setModified(new Date());
        userDTO.setLastLogin(new Date());
        userDTO.setToken("testToken");

        when(userService.getUser(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

    }

    @Test
    @WithMockUser(username = "demotest", roles = "USER")
    void testCreateUser_validUser() throws Exception {
        Usuario user = new Usuario();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("Test1234@");

        UsuarioCreationResponseDTO createdUserDTO = new UsuarioCreationResponseDTO();
        createdUserDTO.setId(1L);
        createdUserDTO.setCreated(new Date());
        createdUserDTO.setModified(new Date());
        createdUserDTO.setLastLogin(new Date());
        createdUserDTO.setToken("testToken");

        when(userService.addUser(any(Usuario.class))).thenReturn(createdUserDTO);

        String requestBody = objectMapper.writeValueAsString(user);

         mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));



    }
}
