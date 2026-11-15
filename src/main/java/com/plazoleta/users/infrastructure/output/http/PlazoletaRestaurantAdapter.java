package com.plazoleta.users.infrastructure.output.http;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.Duration;
import com.plazoleta.users.application.port.output.RestaurantDirectoryPort;
import com.plazoleta.users.domain.exception.AccountException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;

/**
 * Consulta GET /internal/restaurants/{id} en Plazoleta usando el token interno compartido.
 */
@Component
public class PlazoletaRestaurantAdapter implements RestaurantDirectoryPort {
    private final RestClient client;
    private final String internalToken;

    /** Constructor para las pruebas: usa los tiempos de espera por defecto. */
    public PlazoletaRestaurantAdapter(String baseUrl, String internalToken) {
        this(baseUrl, internalToken, HttpClients.DEFAULT_CONNECT_TIMEOUT, HttpClients.DEFAULT_READ_TIMEOUT);
    }

    @Autowired
    public PlazoletaRestaurantAdapter(@Value("${app.plazoleta.base-url}") String baseUrl, @Value("${app.internal.token}") String internalToken,
            @Value("${app.http.connect-timeout:2s}") Duration connectTimeout,
            @Value("${app.http.read-timeout:5s}") Duration readTimeout) {
        this.client = HttpClients.restClient(baseUrl, connectTimeout, readTimeout);
        this.internalToken = internalToken;
    }

    @Override
    public Optional<Long> findOwnerId(long restaurantId) {
        try {
            RestaurantResponse restaurant = client.get().uri("/internal/restaurants/{id}", restaurantId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + internalToken)
                    .retrieve().body(RestaurantResponse.class);
            return Optional.ofNullable(restaurant).map(RestaurantResponse::ownerUserId);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            throw AccountException.dependencyUnavailable("El servicio de restaurantes rechazó la consulta (HTTP "
                    + ex.getStatusCode().value() + ")");
        } catch (RestClientException ex) {
            throw AccountException.dependencyUnavailable("El servicio de restaurantes no está disponible");
        }
    }

    private record RestaurantResponse(Long id, Long ownerUserId) {}
}
