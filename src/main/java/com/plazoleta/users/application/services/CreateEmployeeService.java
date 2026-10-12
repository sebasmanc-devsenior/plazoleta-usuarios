package com.plazoleta.users.application.services;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.ports.input.CreateEmployeeUseCase;
import com.plazoleta.users.application.ports.output.RestaurantDirectoryPort;
import com.plazoleta.users.domain.exceptions.AccountException;
import com.plazoleta.users.domain.models.UserAccount;
import com.plazoleta.users.domain.models.UserRole;

/**
 * HU-6: solo el propietario de un restaurante puede crearle empleados. El dueño se consulta en
 * Plazoleta, que es el servicio dueño de los restaurantes.
 */
public class CreateEmployeeService implements CreateEmployeeUseCase {
    private final AccountRegistrar registrar;
    private final RestaurantDirectoryPort restaurants;

    public CreateEmployeeService(AccountRegistrar registrar, RestaurantDirectoryPort restaurants) {
        this.registrar = registrar;
        this.restaurants = restaurants;
    }

    @Override
    public UserAccount execute(NewAccountCommand command, Long restaurantId, long requesterId) {
        if (restaurantId == null || restaurantId <= 0) {
            throw AccountException.badRequest("A positive restaurantId is required for EMPLEADO accounts");
        }
        long ownerId = restaurants.findOwnerId(restaurantId).orElseThrow(AccountException::restaurantNotFound);
        if (ownerId != requesterId) {
            throw AccountException.forbidden("Only the restaurant owner can create its employees");
        }
        return registrar.register(command, UserRole.EMPLEADO, restaurantId);
    }
}
