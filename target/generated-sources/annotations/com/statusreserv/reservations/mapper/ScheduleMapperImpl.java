package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.ScheduleDto;
import com.statusreserv.reservations.dto.ScheduleTimeDto;
import com.statusreserv.reservations.dto.ScheduleTimeWrite;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-16T00:36:20+0100",
    comments = "version: 1.6.0, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class ScheduleMapperImpl implements ScheduleMapper {

    @Override
    public ScheduleDto toDTO(Schedule schedule) {
        if ( schedule == null ) {
            return null;
        }

        UUID id = null;
        DayOfWeek dayOfWeek = null;
        Set<ScheduleTimeDto> scheduleTime = null;

        id = schedule.getId();
        dayOfWeek = schedule.getDayOfWeek();
        scheduleTime = scheduleTimeSetToScheduleTimeDtoSet( schedule.getScheduleTime() );

        ScheduleDto scheduleDto = new ScheduleDto( id, dayOfWeek, scheduleTime );

        return scheduleDto;
    }

    @Override
    public Schedule toEntity(ScheduleWrite dto, Tenant tenant) {
        if ( dto == null && tenant == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        if ( dto != null ) {
            schedule.setDayOfWeek( dto.dayOfWeek() );
            schedule.setScheduleTime( scheduleTimeWriteSetToScheduleTimeSet( dto.scheduleTime() ) );
        }
        if ( tenant != null ) {
            schedule.setTenant( tenant );
            schedule.setId( tenant.getId() );
            schedule.setCreatedAt( tenant.getCreatedAt() );
        }

        return schedule;
    }

    @Override
    public ScheduleTimeDto toDTO(ScheduleTime scheduleTime) {
        if ( scheduleTime == null ) {
            return null;
        }

        LocalTime openTime = null;
        LocalTime closeTime = null;

        openTime = scheduleTime.getOpenTime();
        closeTime = scheduleTime.getCloseTime();

        ScheduleTimeDto scheduleTimeDto = new ScheduleTimeDto( openTime, closeTime );

        return scheduleTimeDto;
    }

    @Override
    public ScheduleTime toEntity(ScheduleTimeWrite dto) {
        if ( dto == null ) {
            return null;
        }

        ScheduleTime scheduleTime = new ScheduleTime();

        scheduleTime.setOpenTime( dto.openTime() );
        scheduleTime.setCloseTime( dto.closeTime() );

        return scheduleTime;
    }

    protected Set<ScheduleTimeDto> scheduleTimeSetToScheduleTimeDtoSet(Set<ScheduleTime> set) {
        if ( set == null ) {
            return null;
        }

        Set<ScheduleTimeDto> set1 = new LinkedHashSet<ScheduleTimeDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ScheduleTime scheduleTime : set ) {
            set1.add( toDTO( scheduleTime ) );
        }

        return set1;
    }

    protected Set<ScheduleTime> scheduleTimeWriteSetToScheduleTimeSet(Set<ScheduleTimeWrite> set) {
        if ( set == null ) {
            return null;
        }

        Set<ScheduleTime> set1 = new LinkedHashSet<ScheduleTime>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ScheduleTimeWrite scheduleTimeWrite : set ) {
            set1.add( toEntity( scheduleTimeWrite ) );
        }

        return set1;
    }
}
