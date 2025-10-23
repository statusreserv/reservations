package com.statusreserv.reservations.service.reservation;

import com.statusreserv.reservations.dto.reservation.ReservationDTO;
import com.statusreserv.reservations.dto.reservation.ReservationWrite;
import com.statusreserv.reservations.mapper.ReservationMapper;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.repository.ReservationRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<ReservationDTO> findAll() {
        return getAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
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
        var reservationService = write.reservationService()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());
        //reservationService.forEach(s -> s.setReservation(reservation));
        reservation.setServiceReservation(reservationService);
        var entity = repository.save(reservation);
        validator.validateReservation(entity, entity.getId());
        return entity.getId();
    }

    @Transactional
    public void update(UUID id, ReservationWrite write) {
        var existing = getById(id);

        var reservationService = write.reservationService()
                .stream()
                .map(mapper::toEntity)
                .collect(Collectors.toSet());

        existing.setServiceReservation(reservationService);
        existing.setDate(write.date());
        existing.setStartTime(write.startTime());
        existing.setEndTime(write.endTime());
        existing.setTotalPrice(write.totalPrice());

        repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Reservation not found");
        }
        repository.deleteByIdAndTenantId(id, currentUserService.getCurrentTenantId());
    }
}
