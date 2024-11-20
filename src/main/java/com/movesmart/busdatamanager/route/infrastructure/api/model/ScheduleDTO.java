package com.movesmart.busdatamanager.route.infrastructure.api.model;

import com.movesmart.busdatamanager.route.domain.Schedule;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScheduleDTO(
        @NotNull @Enumerated Schedule.TypeOfDay typeOfDay,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull Integer frequencyInMinutes) {
    public Schedule toSchedule() {
        return new Schedule(typeOfDay, startTime, endTime, frequencyInMinutes);
    }

    public static ScheduleDTO fromSchedule(Schedule schedule) {
        return new ScheduleDTO(
                schedule.typeOfDay(), schedule.startTime(), schedule.endTime(), schedule.frequencyInMinutes());
    }
}
