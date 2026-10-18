package com.plazoleta.users.application.port.input;

import com.plazoleta.users.application.dto.NewAccountCommand;
import com.plazoleta.users.domain.model.UserAccount;

/** HU-6: el propietario crea cuentas para los empleados de su restaurante. */
public interface CreateEmployeeUseCase {
    UserAccount execute(NewAccountCommand command, Long restaurantId, long requesterId);
}
