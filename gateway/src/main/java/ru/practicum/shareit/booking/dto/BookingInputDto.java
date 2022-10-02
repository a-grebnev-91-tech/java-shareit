package ru.practicum.shareit.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validation.EndAfterStart;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@EndAfterStart
public class BookingInputDto {
    @NotNull(message = "itemId should not be null")
    private Long itemId;
    @FutureOrPresent(message = "start time should not be in past")
    @NotNull(message = "start time should not be null")
    private LocalDateTime start;
    @Future(message = "end time should not be in past")
    @NotNull(message = "end time should not be null")
    private LocalDateTime end;
}
