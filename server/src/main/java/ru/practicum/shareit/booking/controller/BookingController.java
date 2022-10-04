package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingParamObj;
import ru.practicum.shareit.booking.controller.dto.BookingOutputDto;
import ru.practicum.shareit.booking.controller.dto.BookingInputDto;
import ru.practicum.shareit.booking.domain.BookingService;

import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PatchMapping("{bookingId}")
    public BookingOutputDto approveBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestParam("approved") boolean approved,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User with id {} attempt to set approved = {} for booking {}", userId, approved, bookingId);
        return service.approveBooking(bookingId, userId, approved);
    }

    @PostMapping
    public BookingOutputDto bookItem(
            @RequestHeader(value = USER_ID_HEADER) Long bookerId,
            @RequestBody BookingInputDto dto
    ) {
        log.info("Attempt to create booking by user with id {} for item with id {}", bookerId, dto.getItemId());
        return service.createBooking(dto, bookerId);
    }

    @GetMapping("{bookingId}")
    public BookingOutputDto getBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Attempt to get booking with id {} by user with id {}", bookingId, userId);
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingOutputDto> getAllByBooker(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(value = "state") String state,
            @RequestParam(name = "from") Integer from,
            @RequestParam(name = "size") Integer size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "order") String order
    ) {
        log.info(
                "Booker with id {} attempt to get all his bookings with state {} order by {} {}",
                userId,
                state,
                sortBy,
                order
        );
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(userId).withState(state).from(from)
                .size(size).sortBy(sortBy).sortOrder(order).build();
        return service.getAllBookingsByBooker(paramObj);
    }

    @GetMapping("owner")
    public List<BookingOutputDto> getAllByOwner(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(value = "state") String state,
            @RequestParam(name = "from") Integer from,
            @RequestParam(name = "size") Integer size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "order") String order
    ) {
        log.info(
                "Owner with id {} attempt to get all bookings of his items with state {}",
                userId,
                state
        );
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(userId).withState(state).from(from)
                .size(size).sortBy(sortBy).sortOrder(order).build();
        return service.getAllBookingsByOwner(paramObj);
    }
}
