package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.domain.auth.User;
import com.example.softwaredesigntechniques.repository.auth.UserRepository;
import com.example.softwaredesigntechniques.repository.auth.RoleRepository;
import com.example.softwaredesigntechniques.security.JwtUtils;
import com.example.softwaredesigntechniques.security.CustomUserDetailsService;
import com.example.softwaredesigntechniques.security.AuthTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedpassword");
        testUser.setEmail("test@example.com");
        testUser.setEnabled(true);
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedpassword")).thenReturn(true);
        when(jwtUtils.generateJwtToken(any())).thenReturn("mock-jwt-token");

        String requestBody = "{\n" +
                "    \"username\": \"testuser\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";

        // When & Then
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() throws Exception {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "encodedpassword")).thenReturn(false);

        String requestBody = "{\n" +
                "    \"username\": \"testuser\",\n" +
                "    \"password\": \"wrongpassword\"\n" +
                "}";

        // When & Then
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticateUser_UserNotFound() throws Exception {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        String requestBody = "{\n" +
                "    \"username\": \"nonexistent\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";

        // When & Then
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Given
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String requestBody = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"new@example.com\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() throws Exception {
        // Given
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        String requestBody = "{\n" +
                "    \"username\": \"existinguser\",\n" +
                "    \"email\": \"new@example.com\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        // Given
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        String requestBody = "{\n" +
                "    \"username\": \"newuser\",\n" +
                "    \"email\": \"existing@example.com\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
    }

    @Test
    void testRegisterUser_InvalidRequestBody() throws Exception {
        // Given
        String requestBody = "{\n" +
                "    \"username\": \"\",\n" +
                "    \"email\": \"invalid-email\",\n" +
                "    \"password\": \"123\"\n" +
                "}";

        // When & Then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
