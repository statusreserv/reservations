package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.service.reservation.CancellationService;
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
 * Exposes REST endpoints related to reservation cancellation.
 *
 * <p>This controller allows clients to cancel a reservation either:
 * <ul>
 *   <li>normally – respecting business rules</li>
 *   <li>forcibly – ignoring certain validation rules</li>
 * </ul>
 *
 * A successful cancellation does not return a response body.
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Operations related to reservations management")
public class CancellationController {

    private final CancellationService cancellationService;

    /**
     * Cancels a reservation.
     *
     * <p>By default, a normal cancellation is attempted.
     * If {@code force=true} is sent, a forced cancellation will be executed,
     * bypassing some of the normal business constraints.
     *
     * @param id    the reservation identifier
     * @param force whether the cancellation should be forced (default: false)
     * @return 204 No Content when cancelled successfully
     */
    @PatchMapping("/{id}/cancel")
    @Operation(
            summary = "Cancel a reservation",
            description = """
                    Cancels the reservation identified by the provided ID
                    If `force=true` is provided, a forced cancellation is performed.
                    """)
    @ApiResponse(responseCode = "204", description = "Reservation cancelled successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Reservation not found", content = @Content)
    @ApiResponse(responseCode = "409", description = "Cancellation not allowed", content = @Content)
    public ResponseEntity<Void> cancelReservation(
            @PathVariable
            @Parameter(description = "Reservation unique identifier") UUID id,

            @RequestParam(defaultValue = "false")
            @Parameter(description = "Force cancellation ignoring some business rules") boolean force
    ) {
        cancellationService.cancelReservation(id, force);
        return ResponseEntity.noContent().build();
    }
}
