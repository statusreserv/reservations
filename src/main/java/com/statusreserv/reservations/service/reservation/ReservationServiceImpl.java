package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.mapper.ReservationMapper;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.model.reservation.ReservationStatus;
import com.statusreserv.reservations.repository.ReservationRepository;
import com.statusreserv.reservations.repository.service.ServiceProvided;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import com.statusreserv.reservations.service.service.ServiceProvidedService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service responsible for managing reservations.
 *
 * <p>Handles operations such as creating, retrieving, and updating reservations,
 * including calculating total price and duration, associating provided services,
 * and validating business rules.
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;
    private final ReservationMapper mapper;
    private final CurrentUserService currentUserService;
    private final ReservationValidator validator;
    private final ServiceProvidedService serviceProvidedService;

    /**
     * Retrieves all reservations for the current tenant.
     *
     * @return a list of {@link ReservationDTO} objects
     */
    public List<ReservationDTO> findAll() {
        return getAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all reservation entities for the current tenant.
     *
     * @return a set of {@link Reservation} entities
     */
    public Set<Reservation> getAll() {
        return new HashSet<>(repository.findByTenantId(currentUserService.getCurrentTenantId()));
    }

    /**
     * Retrieves a reservation by its unique identifier and maps it to DTO.
     *
     * @param id the UUID of the reservation
     * @return a {@link ReservationDTO} representing the reservation
     * @throws EntityNotFoundException if no reservation exists with the given ID
     */
    public ReservationDTO findReservation(UUID id) {
        return mapper.toDTO(getById(id));
    }

    /**
     * Retrieves a reservation entity by its unique identifier for the current tenant.
     *
     * @param id the UUID of the reservation
     * @return the {@link Reservation} entity
     * @throws EntityNotFoundException if no reservation exists with the given ID
     */
    public Reservation getById(UUID id) {
        return repository.findByIdAndTenantId(id, currentUserService.getCurrentTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }

    /**
     * Creates a new reservation with the provided data.
     *
     * <p>Associates services provided, calculates total price and duration,
     * sets the reservation status to {@link ReservationStatus#PENDING}, and validates business rules.
     *
     * @param write the data to create the reservation
     * @return the UUID of the newly created reservation
     * @throws IllegalArgumentException if no services are found for the provided IDs
     */
    @Transactional
    public UUID create(ReservationWrite write) {
        var reservation = mapper.toEntity(write, currentUserService.getCurrentTenant());
        var reservationServiceList = getServiceProvidedService(serviceProvidedService.findByIdIn(write.serviceProvidedIds()));

        for (ReservationServiceProvided rs : reservationServiceList) rs.setReservation(reservation);
        reservation.setReservationServices(reservationServiceList);

        if (reservationServiceList.isEmpty()) {
            throw new IllegalArgumentException("No services found for the provided IDs");
        }

        var totalDuration = reservationServiceList
                .stream()
                .mapToInt(ReservationServiceProvided::getDurationMinutes)
                .sum();

        var totalPrice = reservationServiceList
                .stream()
                .map(ReservationServiceProvided::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        reservation.setEndTime(reservation.getStartTime().plusMinutes(totalDuration));
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus(ReservationStatus.PENDING);

        var entity = repository.save(reservation);
        validator.validateReservation(entity, entity.getId());
        return entity.getId();
    }

    /**
     * Updates the status of a reservation.
     *
     * @param id     the UUID of the reservation
     * @param status the new status to set
     * @throws EntityNotFoundException if no reservation exists with the given ID
     */
    @Transactional
    public void updateStatus(UUID id, ReservationStatus status) {
        var existing = getById(id);
        existing.setStatus(status);
        repository.save(existing);
    }

    /**
     * Maps a list of {@link ServiceProvided} to {@link ReservationServiceProvided}
     * for associating with a reservation.
     *
     * @param services the list of services to convert
     * @return a list of {@link ReservationServiceProvided} objects
     */
    private List<ReservationServiceProvided> getServiceProvidedService(List<ServiceProvided> services) {
        return services.stream()
                .map(s -> new ReservationServiceProvided()
                        .withName(s.getName())
                        .withDescription(s.getDescription())
                        .withPrice(s.getPrice())
                        .withDurationMinutes(s.getDurationMinutes())
                        .withServiceProvidedId(s.getId())
                        .withCreatedAt(s.getCreatedAt())
                        .withTenant(s.getTenant()))
                .collect(Collectors.toList());
    }
}
