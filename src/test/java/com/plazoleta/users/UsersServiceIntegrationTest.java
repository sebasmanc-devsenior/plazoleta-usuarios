package com.plazoleta.users;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.BootstrapAdminUseCase;
import com.plazoleta.users.application.port.input.CreateOwnerUseCase;
import com.plazoleta.users.application.port.input.GetUserUseCase;
import com.plazoleta.users.application.port.output.RestaurantDirectoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CreateOwnerUseCase createOwner;
    @Autowired private BootstrapAdminUseCase bootstrapAdmin;
    @Autowired private GetUserUseCase getUser;
    @MockitoBean private RestaurantDirectoryPort restaurants;
    @Value("${app.internal.token}") private String internalToken;

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

    // ---------- HU-2 Consulta interna de usuarios (la usa el servicio de restaurantes) ----------

    @Test
    void internalLookupRequiresServiceBearerTokenAndReturnsValidationFields() throws Exception {
        var owner = createOwner.execute(new NewAccountCommand("Internal", "Lookup", "5001", "+573001234567",
                LocalDate.of(1994, 2, 3), "lookup@example.test", "valid-pass-123"));
        mvc.perform(get("/internal/users/{id}", owner.id()))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/internal/users/{id}", owner.id()).header("Authorization", "Bearer " + internalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(owner.id()))
                .andExpect(jsonPath("$.role").value("PROPIETARIO"))
                .andExpect(jsonPath("$.identityDocument").value("5001"))
                .andExpect(jsonPath("$.phone").value("+573001234567"));
    }

    // ---------- HU-5 Autenticación ----------

    @Test
    void loginIssuesJwtWithUserIdAndRole() throws Exception {
        var owner = createOwner.execute(command("Carlos", "6001", "carlos-6001@example.test"));
        JsonNode claims = JwtsTestSupport.claims(login("CARLOS-6001@example.test"));
        assertThat(claims.get("sub").asText()).isEqualTo(Long.toString(owner.id()));
        assertThat(claims.get("email").asText()).isEqualTo("carlos-6001@example.test");
        assertThat(claims.get("role").asText()).isEqualTo("PROPIETARIO");
    }

    @Test
    void loginWithWrongPasswordReturnsUnauthorized() throws Exception {
        createOwner.execute(command("Lia", "6002", "lia-6002@example.test"));
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"lia-6002@example.test\",\"password\":\"wrong-pass-123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void onlyAdminCanCreateOwners() throws Exception {
        var owner = createOwner.execute(command("Not", "6003", "not-admin-6003@example.test"));
        mvc.perform(post("/api/owners").header("Authorization", "Bearer " + login(owner.email()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(account("Not", "Allowed", "6004", "not-allowed-6004@example.test", null)))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/owners").contentType(MediaType.APPLICATION_JSON)
                        .content(account("No", "Auth", "6005", "no-auth-6005@example.test", null)))
                .andExpect(status().isUnauthorized());
    }

    // ---------- HU-6 Crear empleado ----------

    @Test
    void ownerCreatesEmployeeOnlyForOwnRestaurant() throws Exception {
        var owner = createOwner.execute(command("Owner", "7001", "owner-7001@example.test"));
        when(restaurants.findOwnerId(71L)).thenReturn(Optional.of(owner.id()));
        when(restaurants.findOwnerId(72L)).thenReturn(Optional.of(owner.id() + 1000));
        String ownerToken = login(owner.email());

        String employeeJson = mvc.perform(post("/api/employees").header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(account("Employee", "User", "7002", "employee-7002@example.test", 71)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("EMPLEADO"))
                .andExpect(jsonPath("$.restaurantId").value(71))
                .andReturn().getResponse().getContentAsString();
        var employee = getUser.execute(objectMapper.readTree(employeeJson).get("id").asLong());
        assertThat(JwtsTestSupport.claims(login(employee.email())).get("restaurantId").asLong()).isEqualTo(71);

        mvc.perform(post("/api/employees").header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(account("Other", "Employee", "7003", "employee-7003@example.test", 72)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
    }

    private NewAccountCommand command(String name, String identity, String email) {
        return new NewAccountCommand(name, "Test", identity, "+573001234567", LocalDate.of(1990, 1, 1), email,
                "valid-pass-123");
    }

    private String login(String email) throws Exception {
        String json = mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"valid-pass-123\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(json).get("token").asText();
    }

    /** Desde la HU-5 solo el administrador crea propietarios. */
    private MockHttpServletRequestBuilder asAdmin(MockHttpServletRequestBuilder request) throws Exception {
        bootstrapAdmin.execute(new NewAccountCommand("Admin", "User", "3001", null, null, "admin@example.test",
                "valid-pass-123"));
        return request.header("Authorization", "Bearer " + login("admin@example.test"));
    }

    private String account(String name, String lastName, String identity, String email, Integer restaurantId) {
        String restaurant = restaurantId == null ? "" : ",\"restaurantId\":" + restaurantId;
        return "{\"name\":\"" + name + "\",\"lastName\":\"" + lastName + "\",\"identityDocument\":\"" +
                identity + "\",\"phone\":\"+573001234567\",\"birthDate\":\"1990-01-01\",\"email\":\"" +
                email + "\",\"password\":\"valid-pass-123\"" + restaurant + "}";
    }
}
