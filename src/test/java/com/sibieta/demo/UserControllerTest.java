package com.sibieta.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibieta.demo.config.JwtUtils;
import com.sibieta.demo.config.SecurityConfig;
import com.sibieta.demo.model.Phone;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.UUID;

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
        Usuario user = new Usuario();
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhones(createPhoneList());
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());

        UsuarioDTO userDTO = new UsuarioDTO(user);
        String jwt = "mocked-jwt-token";
        when(userService.getUser(uuid,jwt)).thenReturn(userDTO);        

        mockMvc.perform(get("/user/"+uuid).header("Authorization", "Bearer "+jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

    }

    @Test
    @WithMockUser(username = "demotest", roles = "USER")
    void testCreateUser_validUser() throws Exception {
        Usuario user = new Usuario();
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("Test1234@");

        UsuarioCreationResponseDTO createdUserDTO = new UsuarioCreationResponseDTO();
        createdUserDTO.setId(uuid);
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
                .andExpect(jsonPath("$.id").value(uuid.toString()));

    }

    @Test
    @WithMockUser(username = "demotest", roles = "USER")
    void testGetUserById_serverError() throws Exception {
        UUID uuid = UUID.randomUUID();
        String jwt = "mocked-jwt-token";
        when(userService.getUser(uuid, jwt)).thenThrow(new RuntimeException("Algo estuvo mal!"));

        mockMvc.perform(get("/user/"+uuid).header("Authorization", "Bearer "+jwt))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje").value("Error interno del servidor: Algo estuvo mal!"));
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
                .andExpect(jsonPath("$.mensaje").value("Error interno del servidor: Database error"));
    }

    @Test
    @WithMockUser(username = "demotest", roles = "USER")
    void testGetUser_invalidUuidFormat() throws Exception {
        String invalidUuid = "invalid-uuid";
        String jwt = "mocked-jwt-token";

        mockMvc.perform(get("/user/{id}", invalidUuid).header("Authorization", "Bearer "+jwt))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("Formato invalido de Id"));

    }

    // Helper method to create a list of Phones
    private List<Phone> createPhoneList() {
        List<Phone> phones = new ArrayList<>();
        phones.add(createPhone("57", "1", "3101234567"));
        phones.add(createPhone("57", "1", "3119876543"));
        phones.add(createPhone("57", "2", "3125551212"));
        phones.add(createPhone("57", "3", "3134445566"));
        phones.add(createPhone("57", "4", "3147778899"));
        return phones;
    }

    // Helper method to create a Phone object
    private Phone createPhone(String countryCode, String cityCode, String number) {
        Phone phone = new Phone();
        phone.setContrycode(countryCode);
        phone.setCitycode(cityCode);
        phone.setNumber(number);
        return phone;
    }
}
