package com.statusreserv.reservations.schedule;

import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import com.statusreserv.reservations.service.schedule.ScheduleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleValidatorTest {

    private ScheduleValidator validator;
    private Tenant tenant;

    @BeforeEach
    void setup() {
        validator = new ScheduleValidator();
        tenant = new Tenant();
        tenant.setId(UUID.randomUUID());
    }

    @Test
    void shouldFailWhenOpenTimeIsNull() {
        Schedule schedule = createScheduleWithMultipleTimes(
                new ScheduleTime().withOpenTime(null).withCloseTime(LocalTime.of(10, 0))
        );
        assertThatThrownBy(() -> validator.validateSchedule(schedule))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must have both openTime and closeTime");
    }

    @Test
    void shouldFailWhenCloseTimeIsNull() {
        Schedule schedule = createScheduleWithMultipleTimes(
                new ScheduleTime().withOpenTime(LocalTime.of(8, 0)).withCloseTime(null)
        );
        assertThatThrownBy(() -> validator.validateSchedule(schedule))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must have both openTime and closeTime");
    }

    @Test
    void shouldFailWhenOpenTimeAfterCloseTime() {
        Schedule schedule = createScheduleWithMultipleTimes(
                new ScheduleTime().withOpenTime(LocalTime.of(12, 0)).withCloseTime(LocalTime.of(10, 0))
        );
        assertThatThrownBy(() -> validator.validateSchedule(schedule))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be before closeTime");
    }

    @Test
    void shouldPassForValidTime() {
        Schedule schedule = createScheduleWithMultipleTimes(
                new ScheduleTime().withOpenTime(LocalTime.of(9, 0)).withCloseTime(LocalTime.of(12, 0))
        );
        assertThatCode(() -> validator.validateSchedule(schedule)).doesNotThrowAnyException();
    }

    @Test
    void shouldFailForPartialOverlap() {
        ScheduleTime t1 = new ScheduleTime().withOpenTime(LocalTime.of(9, 0)).withCloseTime(LocalTime.of(11, 0));
        ScheduleTime t2 = new ScheduleTime().withOpenTime(LocalTime.of(10, 30)).withCloseTime(LocalTime.of(12, 0));
        Schedule schedule = createScheduleWithMultipleTimes(t1, t2);
        assertThatThrownBy(() -> validator.validateSchedule(schedule))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Schedule conflict");
    }

    @Test
    void shouldPassForNonOverlappingTimes() {
        ScheduleTime t1 = new ScheduleTime().withOpenTime(LocalTime.of(9, 0)).withCloseTime(LocalTime.of(11, 0));
        ScheduleTime t2 = new ScheduleTime().withOpenTime(LocalTime.of(11, 0)).withCloseTime(LocalTime.of(12, 0));
        Schedule schedule = createScheduleWithMultipleTimes(t1, t2);
        assertThatCode(() -> validator.validateSchedule(schedule)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassForTimesTouchingEdges() {
        ScheduleTime t1 = new ScheduleTime().withOpenTime(LocalTime.of(8, 0)).withCloseTime(LocalTime.of(10, 0));
        ScheduleTime t2 = new ScheduleTime().withOpenTime(LocalTime.of(10, 0)).withCloseTime(LocalTime.of(12, 0));
        Schedule schedule = createScheduleWithMultipleTimes(t1, t2);
        assertThatCode(() -> validator.validateSchedule(schedule)).doesNotThrowAnyException();
    }

    private Schedule createScheduleWithMultipleTimes(ScheduleTime... times) {
        Schedule schedule = new Schedule();
        schedule.setTenant(tenant);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setScheduleTime(new java.util.HashSet<>(java.util.Arrays.asList(times)));
        return schedule;
    }
}
