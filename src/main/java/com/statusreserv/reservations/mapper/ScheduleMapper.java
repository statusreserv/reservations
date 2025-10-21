package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.schedule.ScheduleDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleTimeDTO;
import com.statusreserv.reservations.dto.schedule.ScheduleTimeWrite;
import com.statusreserv.reservations.dto.schedule.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleDTO toDTO(Schedule schedule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", source = "tenant")
    Schedule toEntity(ScheduleWrite dto, Tenant tenant);

    ScheduleTimeDTO toDTO(ScheduleTime scheduleTime);
    ScheduleTime toEntity(ScheduleTimeWrite dto);
}
