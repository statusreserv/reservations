package com.statusreserv.reservations.schedule;

import com.statusreserv.reservations.dto.schedule.ScheduleDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleTimeWrite;
import com.statusreserv.reservations.dto.schedule.ScheduleWrite;
import com.statusreserv.reservations.mapper.ScheduleMapper;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.repository.ScheduleRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import com.statusreserv.reservations.service.schedule.ScheduleService;
import com.statusreserv.reservations.service.schedule.ScheduleServiceImpl;
import com.statusreserv.reservations.service.schedule.ScheduleValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    private ScheduleRepository repository;
    private ScheduleMapper mapper;
    private ScheduleService service;
    private CurrentUserService currentUserService;
    private ScheduleValidator validator;

    @BeforeEach
    void setup() {
        repository = mock(ScheduleRepository.class);
        mapper = mock(ScheduleMapper.class);
        currentUserService = mock(CurrentUserService.class);
        validator = mock(ScheduleValidator.class);
        service = new ScheduleServiceImpl(repository, mapper, currentUserService, validator);
    }

    @Test
    void shouldReturnAllSchedulesAsDto() {
        Schedule schedule = new Schedule();
        schedule.setId(UUID.randomUUID());

        when(repository.findByTenantId(any())).thenReturn(List.of(schedule));
        when(currentUserService.getCurrentTenantId()).thenReturn(UUID.randomUUID());
        when(mapper.toDTO(schedule)).thenReturn(new ScheduleDTO(UUID.randomUUID(), DayOfWeek.MONDAY, Set.of()));

        List<ScheduleDTO> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository, times(1)).findByTenantId(any());
    }

    @Test
    void shouldThrowWhenScheduleNotFound() {
        DayOfWeek day = DayOfWeek.MONDAY;
        when(repository.findByTenantIdAndDayOfWeek(any(), eq(day))).thenReturn(Optional.empty());
        when(currentUserService.getCurrentTenantId()).thenReturn(UUID.randomUUID());

        assertThrows(EntityNotFoundException.class, () -> service.findSchedule(day));
    }

    @Test
    void shouldCreateScheduleViaUpdateWhenNotExist() {
        DayOfWeek day = DayOfWeek.FRIDAY;
        Tenant tenant = new Tenant();
        ScheduleWrite write = new ScheduleWrite(day, Set.of(new ScheduleTimeWrite(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule schedule = new Schedule();
        schedule.setTenant(tenant);

        when(currentUserService.getCurrentTenant()).thenReturn(tenant);
        when(currentUserService.getCurrentTenantId()).thenReturn(UUID.randomUUID());
        when(repository.findByTenantIdAndDayOfWeek(any(), eq(day))).thenReturn(Optional.empty());
        when(mapper.toEntity(write, tenant)).thenReturn(schedule);
        when(mapper.toEntity(any(ScheduleTimeWrite.class))).thenReturn(new ScheduleTime());
        when(repository.save(schedule)).thenReturn(schedule);

        service.update(Set.of(write));

        verify(repository).save(schedule);
        verify(validator).validateSchedule(schedule);
    }

    @Test
    void shouldUpdateExistingScheduleViaUpdate() {
        DayOfWeek existingDay = DayOfWeek.SATURDAY;
        DayOfWeek updateDay = DayOfWeek.SUNDAY;
        Tenant tenant = new Tenant();

        Schedule existing = new Schedule();
        existing.setDayOfWeek(existingDay);
        existing.setTenant(tenant);

        ScheduleWrite write = new ScheduleWrite(updateDay, Set.of(new ScheduleTimeWrite(LocalTime.of(9, 0), LocalTime.of(11, 0))));

        when(currentUserService.getCurrentTenant()).thenReturn(tenant);
        when(currentUserService.getCurrentTenantId()).thenReturn(UUID.randomUUID());
        when(repository.findByTenantIdAndDayOfWeek(any(), eq(updateDay))).thenReturn(Optional.of(existing));
        ScheduleTime scheduleTime = new ScheduleTime();
        when(mapper.toEntity(any(ScheduleTimeWrite.class))).thenReturn(scheduleTime);
        when(repository.save(existing)).thenReturn(existing);

        service.update(Set.of(write));

        assertEquals(updateDay, existing.getDayOfWeek());
        verify(repository).save(existing);
        verify(validator).validateSchedule(existing);
    }

    @Test
    void shouldDeleteScheduleSuccessfully() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);
        when(currentUserService.getCurrentTenantId()).thenReturn(tenantId);

        service.delete(id);

        verify(repository).deleteByIdAndTenantId(id, tenantId);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingSchedule() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(id));
    }

}
