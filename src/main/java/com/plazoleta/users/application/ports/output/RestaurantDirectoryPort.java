package com.plazoleta.users.application.ports.output;

import java.util.Optional;

/**
 * Consulta de restaurantes en el microservicio Plazoleta (dueño de esos datos).
 */
public interface RestaurantDirectoryPort {
    /** Id del propietario del restaurante, o vacío si el restaurante no existe. */
    Optional<Long> findOwnerId(long restaurantId);
}
