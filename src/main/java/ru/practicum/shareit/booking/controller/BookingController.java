package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.domain.BookingService;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingResponse createBooking(
            @RequestHeader(value = USER_ID_HEADER) Long bookerId,
            @RequestBody @Valid BookingRequest dto
    ) {
        log.info("Attempt to create booking by user with id {} for item with id {}", bookerId, dto.getItemId());
        return service.createBooking(dto, bookerId);
    }

    @GetMapping("{bookingId}")
    public BookingResponse getBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Attempt to get booking with id {} by user with id {}", bookingId, userId);
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponse> getAllByBooker(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "sortBy", defaultValue = "start") String sortBy,
            @RequestParam(value = "order", defaultValue = "DESC") String order
    ) {
        log.info(
                "Booker with id {} attempt to get all his bookings with state {} order by {} {}",
                userId,
                state,
                sortBy,
                order
        );
        return service.getAllBookingsByBooker(userId, state, sortBy, order);
    }

    @GetMapping("owner")
    public List<BookingResponse> getAllByOwner(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state
    ) {
        log.info(
                "Owner with id {} attempt to get all bookings of his items with state {}",
                userId,
                state
        );
        return service.getAllBookingsByOwner(userId, state);
    }

    @PatchMapping("{bookingId}")
    public BookingResponse patchBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestParam("approved") boolean approved,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User with id {} attempt to set approved = {} for booking {}", userId, approved, bookingId);
        return service.approveBooking(bookingId, userId, approved);
    }
}
