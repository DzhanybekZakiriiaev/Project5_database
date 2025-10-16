package com.example.softwaredesigntechniques.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for Spring Security configuration.
 * This test class verifies that security rules are correctly applied
 * to public, protected, and admin endpoints.
 */
@WebMvcTest
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @Test
    void testAuthEndpoints_NoAuthenticationRequired() throws Exception {
        // Signup endpoint should be accessible without authentication
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest()); // Bad request due to invalid JSON, not 401
        
        // Signin endpoint should be accessible without authentication
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest()); // Bad request due to invalid JSON, not 401
    }

    @Test
    void testSwaggerEndpoints_NoAuthenticationRequired() throws Exception {
        // Swagger UI should be accessible without authentication
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());
        
        // API docs should be accessible without authentication
        mockMvc.perform(get("/api/docs"))
                .andExpect(status().isOk());
    }

    @Test
    void testProtectedEndpoints_NoAuthenticationRequired_Returns401() throws Exception {
        // Protected endpoints should return 401 without authentication
        mockMvc.perform(get("/api/facilities/machines"))
                .andExpect(status().isUnauthorized());
        
        mockMvc.perform(get("/api/inventory/items"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testProtectedEndpoints_WithUserRole_CanAccess() throws Exception {
        // With USER role, protected endpoints should be accessible (though may have other errors)
        mockMvc.perform(get("/api/facilities/machines"))
                .andExpect(status().isInternalServerError()); // DB error, but not 403
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAuthenticatedUser_CanAccessPublicEndpoints() throws Exception {
        // Even with a different role, swagger should still be accessible
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthenticatedUser_CanAccessPublicEndpoints() throws Exception {
        // Public endpoints should always be accessible
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}
