package com.plazoleta.users.application.usecase;

import com.plazoleta.users.domain.util.MensajesError;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.application.port.input.CreateEmployeePort;
import com.plazoleta.users.application.port.output.RestaurantDirectoryPort;
import com.plazoleta.users.domain.exception.AccountException;
import com.plazoleta.users.domain.model.UserAccount;
import com.plazoleta.users.domain.model.UserRole;

/**
 * HU-6: solo el propietario de un restaurante puede crearle empleados. El dueño se consulta en
 * Plazoleta, que es el servicio dueño de los restaurantes.
 */
public class CreateEmployeeUseCase implements CreateEmployeePort {
    private final AccountRegistrar registrar;
    private final RestaurantDirectoryPort restaurants;

    public CreateEmployeeUseCase(AccountRegistrar registrar, RestaurantDirectoryPort restaurants) {
        this.registrar = registrar;
        this.restaurants = restaurants;
    }

    @Override
    public UserAccount execute(NewAccountCommand command, Long restaurantId, long requesterId) {
        if (restaurantId == null || restaurantId <= 0) {
            throw AccountException.badRequest(MensajesError.CUENTAS_DE_EMPLEADO_NECESITAN_UN_RESTAURANTID_POSITIVO);
        }
        long ownerId = restaurants.findOwnerId(restaurantId).orElseThrow(AccountException::restaurantNotFound);
        if (ownerId != requesterId) {
            throw AccountException.forbidden(MensajesError.SOLO_EL_PROPIETARIO_DEL_RESTAURANTE_PUEDE_CREAR);
        }
        return registrar.register(command, UserRole.EMPLEADO, restaurantId);
    }
}
