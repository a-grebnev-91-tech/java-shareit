package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.model.BookingService;

import javax.validation.Valid;

import static ru.practicum.shareit.util.Сonstant.USER_ID_HEADER;

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

//    GET /bookings/{bookingId}
    @GetMapping("{bookingId}")
    public BookingResponse getBooking(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Attempt to get booking with id {} by user with id {}", bookingId, userId);
        return service.getBooking(bookingId, userId);
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
