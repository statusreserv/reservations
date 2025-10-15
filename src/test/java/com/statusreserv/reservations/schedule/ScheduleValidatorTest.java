package com.statusreserv.reservations.schedule;

import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.repository.ScheduleRepository;
import com.statusreserv.reservations.service.ScheduleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ScheduleValidatorTest {

    private ScheduleValidator validator;
    private ScheduleRepository repository;
    private Tenant tenant;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(ScheduleRepository.class);
        validator = new ScheduleValidator(repository);
        tenant = new Tenant();
        tenant.setId(UUID.randomUUID());
    }

    @Test
    void validScheduleWithoutOverlap() {
        Schedule schedule = new Schedule();
        schedule.setTenant(tenant);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setScheduleTime(Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), DayOfWeek.MONDAY)).thenReturn(List.of());

        assertDoesNotThrow(() -> validator.validateSchedule(schedule, null),
                "Expected: schedule is valid and does not throw an exception. Test failed if an exception occurred.");
    }

    @Test
    void incomingExactlyMatchesExisting() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(incoming, null),
                "Expected: exception due to full overlap. Test failed if no exception occurred.");
    }

    @Test
    void incomingEnclosesExisting() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(7, 0), LocalTime.of(15, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(incoming, null),
                "Expected: exception because incoming schedule encloses existing one. Test failed if no exception occurred.");
    }

    @Test
    void incomingInsideExisting() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(9, 0), LocalTime.of(11, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(incoming, null),
                "Expected: exception because incoming schedule is inside existing one. Test failed if no exception occurred.");
    }

    @Test
    void incomingOverlapsStartOfExisting() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(7, 0), LocalTime.of(9, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(incoming, null),
                "Expected: exception due to partial overlap at start. Test failed if no exception occurred.");
    }

    @Test
    void incomingOverlapsEndOfExisting() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(11, 0), LocalTime.of(13, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(incoming, null),
                "Expected: exception due to partial overlap at end. Test failed if no exception occurred.");
    }

    @Test
    void incomingTouchesEndOfExisting() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(12, 0), LocalTime.of(14, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertDoesNotThrow(() -> validator.validateSchedule(incoming, null),
                "Expected: schedule allowed when touching end of existing schedule. Test failed if exception occurred.");
    }

    @Test
    void incomingTouchesStartOfExisting() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(6, 0), LocalTime.of(8, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertDoesNotThrow(() -> validator.validateSchedule(incoming, null),
                "Expected: schedule allowed when touching start of existing schedule. Test failed if exception occurred.");
    }

    @Test
    void incomingDoesNotOverlapDifferentTimes() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(13, 0), LocalTime.of(15, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertDoesNotThrow(() -> validator.validateSchedule(incoming, null),
                "Expected: schedule valid as times do not overlap. Test failed if exception occurred.");
    }

    @Test
    void incomingOverlapsMultipleExistingTimes() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(
                        new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0)),
                        new ScheduleTime(LocalTime.of(14, 0), LocalTime.of(16, 0))
                ));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(15, 0), LocalTime.of(17, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(incoming, null),
                "Expected: exception due to overlap with multiple existing times. Test failed if no exception occurred.");
    }

    @Test
    void incomingTouchesMultipleExistingTimes() {
        DayOfWeek day = DayOfWeek.MONDAY;
        Schedule existing = buildSchedule(UUID.randomUUID(), day,
                Set.of(
                        new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0)),
                        new ScheduleTime(LocalTime.of(14, 0), LocalTime.of(16, 0))
                ));
        Schedule incoming = buildSchedule(null, day,
                Set.of(new ScheduleTime(LocalTime.of(12, 0), LocalTime.of(14, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), day)).thenReturn(List.of(existing));

        assertDoesNotThrow(() -> validator.validateSchedule(incoming, null),
                "Expected: schedule allowed when touching multiple existing times. Test failed if exception occurred.");
    }

    @Test
    void invalidTimeStartAfterEnd() {
        ScheduleTime invalidTime = new ScheduleTime(LocalTime.of(10, 0), LocalTime.of(9, 0));
        Schedule schedule = new Schedule();
        schedule.setTenant(new Tenant());
        schedule.setDayOfWeek(DayOfWeek.TUESDAY);
        schedule.setScheduleTime(Set.of(invalidTime));

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(schedule, null),
                "Expected: exception due to start time after end time. Test failed if no exception occurred.");
        assertTrue(ex.getMessage().contains("Invalid ScheduleTime"));
    }

    @Test
    void overlappingSchedulesShouldThrow() {
        Tenant tenant = new Tenant();
        tenant.setId(UUID.randomUUID());

        Schedule existing = new Schedule();
        existing.setId(UUID.randomUUID());
        existing.setTenant(tenant);
        existing.setDayOfWeek(DayOfWeek.WEDNESDAY);
        existing.setScheduleTime(Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Schedule newSchedule = new Schedule();
        newSchedule.setTenant(tenant);
        newSchedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
        newSchedule.setScheduleTime(Set.of(new ScheduleTime(LocalTime.of(11, 0), LocalTime.of(13, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), DayOfWeek.WEDNESDAY)).thenReturn(List.of(existing));

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> validator.validateSchedule(newSchedule, null),
                "Expected: exception due to overlapping schedule. Test failed if no exception occurred.");
        assertTrue(ex.getMessage().contains("overlapping time"));
    }

    @Test
    void updatingSameScheduleShouldIgnoreOverlap() {
        Tenant tenant = new Tenant();
        UUID id = UUID.randomUUID();

        Schedule existing = new Schedule();
        existing.setId(id);
        existing.setTenant(tenant);
        existing.setDayOfWeek(DayOfWeek.THURSDAY);
        existing.setScheduleTime(Set.of(new ScheduleTime(LocalTime.of(8, 0), LocalTime.of(12, 0))));

        Schedule updating = new Schedule();
        updating.setId(id);
        updating.setTenant(tenant);
        updating.setDayOfWeek(DayOfWeek.THURSDAY);
        updating.setScheduleTime(Set.of(new ScheduleTime(LocalTime.of(9, 0), LocalTime.of(11, 0))));

        when(repository.findByTenantIdAndDayOfWeek(tenant.getId(), DayOfWeek.THURSDAY)).thenReturn(List.of(existing));

        assertDoesNotThrow(() -> validator.validateSchedule(updating, id),
                "Expected: updating same schedule should not throw an exception. Test failed if exception occurred.");
    }

    private Schedule buildSchedule(UUID id, DayOfWeek day, Set<ScheduleTime> times) {
        Schedule s = new Schedule();
        s.setId(id);
        s.setTenant(tenant);
        s.setDayOfWeek(day);
        s.setScheduleTime(times);
        return s;
    }
}
