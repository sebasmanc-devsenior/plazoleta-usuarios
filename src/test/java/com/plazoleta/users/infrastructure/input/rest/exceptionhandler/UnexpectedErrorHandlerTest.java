package com.plazoleta.users.infrastructure.input.rest.exceptionhandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.plazoleta.users.domain.util.MensajesError;

/** Un error no previsto responde 500 con el formato del proyecto, sin detalles internos. */
class UnexpectedErrorHandlerTest {
    private final ApiExceptionHandler handler = new ApiExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/algo");

    @Test
    void anUnexpectedErrorAnswers500WithoutInternalDetails() throws Exception {
        var response = handler.unexpected(new RuntimeException("NullPointer en la línea 42 con SQL interno"), request);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody().message()).isEqualTo(MensajesError.ERROR_INTERNO_DEL_SERVIDOR);
        assertThat(response.getBody().message()).doesNotContain("SQL").doesNotContain("42");
    }

    @Test
    void aMethodThatIsNotAllowedKeepsItsOwnCode() throws Exception {
        var response = handler.unexpected(new HttpRequestMethodNotSupportedException("PATCH"), request);

        assertThat(response.getStatusCode().value()).isEqualTo(405);
        assertThat(response.getBody().message()).isEqualTo(MensajesError.PETICION_NO_SOPORTADA);
    }

    @Test
    void anAccessDeniedIsLeftToSpringSecurity() {
        assertThatThrownBy(() -> handler.unexpected(new org.springframework.security.access.AccessDeniedException("x"), request))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class);
    }
}
