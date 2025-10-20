package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.ScheduleDto;
import com.statusreserv.reservations.dto.ScheduleTimeDto;
import com.statusreserv.reservations.dto.ScheduleTimeWrite;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleDto toDTO(Schedule schedule);

    @Mapping(target = "tenant", source = "batatas")
    Schedule toEntity(ScheduleWrite dto, Tenant batatas);
    
    ScheduleTimeDto toDTO(ScheduleTime scheduleTime);
    ScheduleTime toEntity(ScheduleTimeWrite dto);
}
