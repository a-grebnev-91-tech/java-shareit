package ru.practicum.shareit.booking.controller.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.util.validation.EndAfterStart;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Getter
@Setter
@EndAfterStart
public class BookingInputDto {
    @Null
    private Long bookerId;
    @NotNull(message = "itemId should not be null")
    private Long itemId;
    @Future(message = "start time should not be in past")
    @NotNull(message = "start time should not be null")
    private LocalDateTime start;
    @Future(message = "end time should not be in past")
    @NotNull(message = "end time should not be null")
    private LocalDateTime end;
}
