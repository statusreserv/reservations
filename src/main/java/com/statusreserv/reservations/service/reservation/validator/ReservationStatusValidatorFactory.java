package com.statusreserv.reservations.service.reservation.validator;

import com.statusreserv.reservations.constants.ValidatorType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory to provide the appropriate ReservationStatusValidator
 * implementation based on the type of reservation status change.
 */
@Component
public class ReservationStatusValidatorFactory {

    private final Map<ValidatorType, ReservationStatusValidator> validators;

    public ReservationStatusValidatorFactory(
            CancellationValidator cancelReservationValidator,
            ConfirmationValidator confirmReservationValidator) {
        this.validators = Map.of(
                ValidatorType.CANCEL, cancelReservationValidator,
                ValidatorType.CONFIRM, confirmReservationValidator
        );
    }

    /**
     * Returns the appropriate validator for the given type.
     *
     * @param type type of reservation status validation
     * @return corresponding ReservationStatusValidator
     */
    public ReservationStatusValidator getValidator(ValidatorType type) {
        var validator = validators.get(type);
        if (validator == null) {
            throw new IllegalArgumentException("No validator found for type: " + type);
        }
        return validator;
    }

}
