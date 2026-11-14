package com.plazoleta.users.infrastructure.output.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.plazoleta.users.domain.exception.AccountException;

/** HU-6: si restaurantes se cuelga, crear un empleado responde 503 a tiempo en vez de quedarse esperando. */
class HttpTimeoutsTest {
    /** Servidor que acepta la conexión pero nunca responde: así se ve un servicio colgado. */
    private ServerSocket silentServer;

    @BeforeEach
    void openSilentServer() throws IOException {
        silentServer = new ServerSocket(0, 50, InetAddress.getLoopbackAddress());
    }

    @AfterEach
    void closeSilentServer() throws IOException {
        silentServer.close();
    }

    private String silentUrl() {
        return "http://127.0.0.1:" + silentServer.getLocalPort();
    }

    private static final Duration SHORT = Duration.ofMillis(300);

    @Test
    void theRestaurantsServiceHangingAnswersUnavailableInTime() {
        PlazoletaRestaurantAdapter restaurants = new PlazoletaRestaurantAdapter(silentUrl(), "token", SHORT, SHORT);
        long start = System.nanoTime();

        assertThatThrownBy(() -> restaurants.findOwnerId(1L))
                .isInstanceOfSatisfying(AccountException.class, ex -> assertThat(ex.status()).isEqualTo(503));
        assertThat(Duration.ofNanos(System.nanoTime() - start)).isLessThan(Duration.ofSeconds(3));
    }
}
