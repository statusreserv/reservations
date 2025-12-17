package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.mapper.ReservationMapper;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.model.reservation.Status;
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

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;
    private final ReservationMapper mapper;
    private final CurrentUserService currentUserService;
    private final ReservationValidator validator;
    private final ServiceProvidedService serviceProvidedService;

    public List<ReservationDTO> findAll() {
        return getAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public Set<Reservation> getAll() {
        return new HashSet<>(repository.findByTenantId(currentUserService.getCurrentTenantId()));
    }

    public ReservationDTO findReservation(UUID id) {
        return mapper.toDTO(getById(id));
    }

    public Reservation getById(UUID id) {
        return repository.findByIdAndTenantId(id, currentUserService.getCurrentTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }

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
        reservation.setStatus(Status.PENDING);

        var entity = repository.save(reservation);
        validator.validateReservation(entity, entity.getId());
        return entity.getId();
    }

    @Transactional
    public void updateStatus(UUID id, Status status) {
        var existing = getById(id);
        existing.setStatus(status);
        repository.save(existing);
    }

    private List<ReservationServiceProvided> getServiceProvidedService(List<ServiceProvided> services) {
        return services.stream().map(s -> new ReservationServiceProvided()
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
