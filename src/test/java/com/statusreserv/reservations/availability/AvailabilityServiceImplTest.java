package com.statusreserv.reservations.availability;

import com.statusreserv.reservations.dto.availability.AvailabilityRequestDTO;
import com.statusreserv.reservations.dto.availability.TimeRangeDTO;
import com.statusreserv.reservations.dto.service.ServiceProvidedDTO;
import com.statusreserv.reservations.mapper.ServiceProvidedMapper;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.repository.service.ServiceProvided;
import com.statusreserv.reservations.repository.ReservationRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import com.statusreserv.reservations.service.availability.AvailabilityServiceImpl;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import com.statusreserv.reservations.service.service.ServiceProvidedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AvailabilityServiceImplTest {

    @Mock
    private ServiceProvidedService serviceProvidedService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ServiceProvidedMapper serviceProvidedMapper;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAvailability_returnsAvailableSlots() {
        var from = LocalDate.of(2025, 11, 10);
        var to = LocalDate.of(2025, 11, 10);
        var serviceId = UUID.randomUUID();
        var request = new AvailabilityRequestDTO(from, to, Set.of(serviceId));

        var service = new ServiceProvided();
        service.setId(serviceId);
        service.setDurationMinutes(30);
        when(serviceProvidedService.findByIdIn(Set.of(serviceId))).thenReturn(List.of(service));

        var serviceDTO = new ServiceProvidedDTO(serviceId, "Test Service", "desc", BigDecimal.TEN, 30);
        when(serviceProvidedMapper.toDTO(service)).thenReturn(serviceDTO);

        var schedule = new Schedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setScheduleTime(Set.of(new ScheduleTime(LocalTime.of(9, 0), LocalTime.of(10, 0))));
        var list = new HashSet<Schedule>();
        list.add(schedule);
        when(scheduleService.getAll()).thenReturn(list);

        when(currentUserService.getCurrentTenantId()).thenReturn(UUID.randomUUID());
        when(reservationRepository.findByDateBetweenAndStatusInAndTenantId(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        var result = availabilityService.findAvailability(request);

        assertThat(result).isNotNull();
        assertThat(result.timeSlots()).isNotEmpty();
        assertThat(result.services()).contains(serviceDTO);
    }

    @Test
    void testGetPeriods_returnsCorrectMap() {
        var monday = LocalDate.of(2025, 11, 10); // Monday

        var schedule = new Schedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setScheduleTime(Set.of(new ScheduleTime(LocalTime.of(9, 0), LocalTime.of(17, 0))));

        var list = new HashSet<Schedule>();
        list.add(schedule);

        when(scheduleService.getAll()).thenReturn(list);

        var result = availabilityService.getPeriods(List.of(monday));

        assertThat(result).containsKey(monday);
        var timeRanges = result.get(monday);
        assertThat(timeRanges).hasSize(1);
        assertThat(timeRanges.get(0).start()).isEqualTo(LocalTime.of(9, 0));
        assertThat(timeRanges.get(0).end()).isEqualTo(LocalTime.of(17, 0)); // podes também validar o fim
    }


    @Test
    void testGetAvailableTimeSlots_noReservations() {
        LocalDate date = LocalDate.of(2025, 11, 10);
        var timeRange = new TimeRangeDTO(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var periods = Map.of(date, List.of(timeRange));

        when(currentUserService.getCurrentTenantId()).thenReturn(UUID.randomUUID());
        when(reservationRepository.findByDateBetweenAndStatusInAndTenantId(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        var slots = availabilityService.getAvailableTimeSlots(periods, 30);

        assertThat(slots).isNotEmpty();
        assertThat(slots.iterator().next().date()).isEqualTo(date);
    }

    @Test
    void testGetAvailableTimeSlots_withOverlappingReservation_excludesSlot() {
        LocalDate date = LocalDate.of(2025, 11, 10);
        var timeRange = new TimeRangeDTO(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var periods = Map.of(date, List.of(timeRange));

        var reservation = new Reservation();
        reservation.setDate(date);
        reservation.setStartTime(LocalTime.of(9, 0));
        reservation.setEndTime(LocalTime.of(9, 30));

        when(currentUserService.getCurrentTenantId()).thenReturn(UUID.randomUUID());
        when(reservationRepository.findByDateBetweenAndStatusInAndTenantId(any(), any(), any(), any()))
                .thenReturn(List.of(reservation));

        var slots = availabilityService.getAvailableTimeSlots(periods, 30);

        assertThat(slots).isNotEmpty();
        assertThat(slots.iterator().next().timeRange().start()).isEqualTo(LocalTime.of(9, 30));
    }

    @Test
    void testGetAvailableTimeSlots_emptyPeriods_returnsEmptySet() {
        var result = availabilityService.getAvailableTimeSlots(Collections.emptyMap(), 30);
        assertThat(result).isEmpty();
    }
}
