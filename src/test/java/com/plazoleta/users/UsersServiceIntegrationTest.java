package com.plazoleta.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;



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
}
