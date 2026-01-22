package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.service.reservation.ConfirmationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Exposes REST endpoints related to reservation confirmation.
 *
 * <p>This controller allows clients to confirm a reservation either:
 * <ul>
 *   <li>normally – respecting all business rules</li>
 *   <li>forcibly – ignoring certain validation rules</li>
 * </ul>
 *
 * A successful confirmation does not return a response body.
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Operations related to reservation management")
public class ConfirmationController {

    private final ConfirmationService confirmationService;

    /**
     * Confirms a reservation.
     *
     * <p>By default, a normal confirmation is attempted.
     * If {@code force=true} is sent, a forced confirmation will be executed,
     * bypassing some of the normal business constraints.
     *
     * @param id    the reservation identifier
     * @param force whether the confirmation should be forced (default: false)
     * @return 204 No Content when confirmed successfully
     */
    @PatchMapping("/{id}/confirm")
    @Operation(
            summary = "Confirm a reservation",
            description = """
                    Confirms the reservation identified by the provided ID.
                    If `force=true` is provided, a forced confirmation is performed,
                    ignoring certain validation rules.
                    """
    )
    @ApiResponse(responseCode = "204", description = "Reservation confirmed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Reservation not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Confirmation not allowed", content = @Content)
    public ResponseEntity<Void> confirmReservation(
            @PathVariable
            @Parameter(description = "Reservation unique identifier")
            UUID id,

            @RequestParam(defaultValue = "false")
            @Parameter(description = "Force confirmation ignoring some business rules")
            boolean force
    ) {
        confirmationService.confirmReservation(id, force);
        return ResponseEntity.noContent().build();
    }
}
