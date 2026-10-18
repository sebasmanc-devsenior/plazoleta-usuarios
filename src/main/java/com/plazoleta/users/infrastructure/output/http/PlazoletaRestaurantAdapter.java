package com.plazoleta.users.infrastructure.output.http;

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

    public PlazoletaRestaurantAdapter(@Value("${app.plazoleta.base-url}") String baseUrl,
                                      @Value("${app.internal.token}") String internalToken) {
        this.client = RestClient.builder().baseUrl(baseUrl).build();
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
            throw AccountException.dependencyUnavailable("Plazoleta rejected the restaurant lookup (HTTP "
                    + ex.getStatusCode().value() + ")");
        } catch (RestClientException ex) {
            throw AccountException.dependencyUnavailable("Plazoleta service is unavailable");
        }
    }

    private record RestaurantResponse(Long id, Long ownerUserId) {}
}
