package com.plazoleta.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba el contrato REST completo (seguridad, validación, JWT) sobre H2 en memoria
 * (src/test/resources/application.properties). Nunca toca la base MySQL real.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UsersServiceIntegrationTest {
    @Autowired private MockMvc mvc;

    @Test
    void contextLoads() {
    }

    // ---------- HU-1 Crear propietario ----------

    @Test
    void createsOwnerWithRoleOwnerAndNeverExposesThePassword() throws Exception {
        mvc.perform(asAdmin(post("/api/owners")).contentType(MediaType.APPLICATION_JSON)
                        .content(account("Owner", "User", "1001", "owner-1001@example.test", null)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("PROPIETARIO"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void rejectsUnderageOwner() throws Exception {
        mvc.perform(asAdmin(post("/api/owners")).contentType(MediaType.APPLICATION_JSON)
                        .content(account("Young", "Owner", "1002", "young-1002@example.test", null)
                                .replace("1990-01-01", LocalDate.now().minusYears(17).toString())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("UNDERAGE_USER"));
    }

    @Test
    void rejectsDuplicateEmailIgnoringCaseAndUsesErrorContract() throws Exception {
        mvc.perform(asAdmin(post("/api/owners")).contentType(MediaType.APPLICATION_JSON)
                        .content(account("One", "Person", "2001", "one@example.test", null)))
                .andExpect(status().isCreated());
        mvc.perform(asAdmin(post("/api/owners")).contentType(MediaType.APPLICATION_JSON)
                        .content(account("Two", "Person", "2002", "ONE@example.test", null)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("ACCOUNT_CONFLICT"))
                .andExpect(jsonPath("$.path").value("/api/owners"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    /** Hasta la HU-5 no hay autenticación: crear propietarios no requiere token. */
    private MockHttpServletRequestBuilder asAdmin(MockHttpServletRequestBuilder request) {
        return request;
    }

    private String account(String name, String lastName, String identity, String email, Integer restaurantId) {
        String restaurant = restaurantId == null ? "" : ",\"restaurantId\":" + restaurantId;
        return "{\"name\":\"" + name + "\",\"lastName\":\"" + lastName + "\",\"identityDocument\":\"" +
                identity + "\",\"phone\":\"+573001234567\",\"birthDate\":\"1990-01-01\",\"email\":\"" +
                email + "\",\"password\":\"valid-pass-123\"" + restaurant + "}";
    }
}
