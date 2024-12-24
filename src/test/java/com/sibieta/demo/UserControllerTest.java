package com.sibieta.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibieta.demo.config.SecurityConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    private UserDetails testUser;

    @BeforeEach
    void setUp() {
        testUser = User.withUsername("demotest")
                .password("12345678")
                .roles("USER")
                .build();

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "demotest", roles = "USER")
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

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        // mockMvc.perform(get("/user/1"))
        // .andExpect(status().isFound())
        // .andExpect(MockMvcResultMatchers.redirectedUrl("/swagger-ui/index.html"));

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

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    @WithMockUser(username = "demotest", roles = "USER")
    void testGetUserById_serverError() throws Exception {
        when(userService.getUser(1L)).thenThrow(new RuntimeException("Something went wrong!"));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isInternalServerError()) // Expect 500
                .andExpect(jsonPath("$.mensaje").value("Internal Server Error: Something went wrong!"));
    }

    @Test
    @WithMockUser(username = "demotest", roles = "USER")
    void testCreateUser_serverError() throws Exception {
        Usuario user = new Usuario();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("Test1234@");

        when(userService.addUser(any(Usuario.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje").value("Internal Server Error: Database error"));
    }
}
