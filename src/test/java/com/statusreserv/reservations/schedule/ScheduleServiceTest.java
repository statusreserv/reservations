package com.statusreserv.reservations.schedule;

import com.statusreserv.reservations.dto.ScheduleDTO;
import com.statusreserv.reservations.dto.ScheduleTimeWrite;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.mapper.ScheduleMapper;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.repository.ScheduleRepository;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import com.statusreserv.reservations.service.schedule.ScheduleServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    private ScheduleRepository repository;
    private ScheduleMapper mapper;
    private ScheduleService service;

    @BeforeEach
    void setup() {
        repository = mock(ScheduleRepository.class);
        mapper = mock(ScheduleMapper.class);
        service = new ScheduleServiceImpl(repository, mapper);
    }

    @Test
    void shouldReturnAllSchedulesAsDto() {
        Schedule schedule = new Schedule();
        schedule.setId(UUID.randomUUID());

        when(repository.findAll()).thenReturn(List.of(schedule));
        when(mapper.toDTO(schedule)).thenReturn(new ScheduleDTO(UUID.randomUUID(), DayOfWeek.MONDAY, Set.of()));

        List<ScheduleDTO> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldThrowWhenScheduleNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findSchedule(id));
    }

    @Test
    void shouldCreateScheduleSuccessfully() {
        ScheduleWrite write = new ScheduleWrite(DayOfWeek.FRIDAY, Set.of(new ScheduleTimeWrite(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule schedule = new Schedule();
        schedule.setId(UUID.randomUUID());
        schedule.setTenant(new Tenant());

        when(mapper.toEntity(any(), any())).thenReturn(schedule);
        when(repository.save(any(Schedule.class))).thenReturn(schedule);

        UUID id = service.create(write);

        assertNotNull(id);
        verify(repository).save(any(Schedule.class));
    }

    @Test
    void shouldUpdateScheduleSuccessfully() {
        UUID id = UUID.randomUUID();
        Schedule existing = new Schedule();
        existing.setId(id);
        existing.setDayOfWeek(DayOfWeek.SATURDAY);

        ScheduleWrite write = new ScheduleWrite(DayOfWeek.SUNDAY, Set.of(new ScheduleTimeWrite(LocalTime.of(9, 0), LocalTime.of(11, 0))));

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Schedule.class))).thenReturn(existing);


        verify(repository).save(existing);
    }

    @Test
    void shouldDeleteScheduleSuccessfully() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingSchedule() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(id));
    }
}
