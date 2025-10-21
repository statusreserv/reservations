package com.statusreserv.reservations.mapper;

import com.statusreserv.reservations.dto.ScheduleDTO;
import com.statusreserv.reservations.dto.ScheduleTimeDTO;
import com.statusreserv.reservations.dto.ScheduleTimeWrite;
import com.statusreserv.reservations.dto.ScheduleWrite;
import com.statusreserv.reservations.model.schedule.Schedule;
import com.statusreserv.reservations.model.schedule.ScheduleTime;
import com.statusreserv.reservations.model.tenant.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleDTO toDTO(Schedule schedule);

    @Mapping(target = "tenant", source = "tenant")
    Schedule toEntity(ScheduleWrite dto, Tenant tenant);
    
    ScheduleTimeDTO toDTO(ScheduleTime scheduleTime);
    ScheduleTime toEntity(ScheduleTimeWrite dto);
}
