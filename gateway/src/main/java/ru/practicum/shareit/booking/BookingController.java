package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.validation.ValidBookingState;
import ru.practicum.shareit.validation.ValidSortOrder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.util.Constants.*;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PatchMapping("{bookingId}")
	public ResponseEntity<Object> approveBooking(
			@PathVariable("bookingId") @Positive long bookingId,
			@RequestParam("approved") boolean approved,
			@RequestHeader(USER_ID_HEADER) @Positive long userId) {
		log.info("User with id {} attempt to set approved = {} for booking {}", userId, approved, bookingId);
		return bookingClient.approveBooking(bookingId, userId, approved);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(
			@RequestHeader(USER_ID_HEADER) long userId,
			@RequestBody @Valid BookingInputDto dto) {
		log.info("Creating booking {}, userId={}", dto, userId);
		return bookingClient.bookItem(userId, dto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(
			@RequestHeader(USER_ID_HEADER) long userId,
			@PathVariable @Positive Long bookingId
	) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByBooker(
			@RequestHeader(USER_ID_HEADER) long userId,
			@RequestParam(name = "state", defaultValue = BOOKING_DEFAULT_STATE) @ValidBookingState String state,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
			@RequestParam(value = "sortBy", defaultValue = BOOKING_DEFAULT_SORT_BY) String sortBy,
			@RequestParam(value = "order", defaultValue = BOOKING_DEFAULT_ORDER) @ValidSortOrder String order
	) {
		log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
		return bookingClient.getBookingsByBooker(userId, state, from, size, sortBy, order);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsByOwner(
			@RequestHeader(USER_ID_HEADER) long userId,
			@RequestParam(name = "state", defaultValue = BOOKING_DEFAULT_STATE) @ValidBookingState String state,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
			@RequestParam(value = "sortBy", defaultValue = BOOKING_DEFAULT_SORT_BY) String sortBy,
			@RequestParam(value = "order", defaultValue = BOOKING_DEFAULT_ORDER) @ValidSortOrder String order
	) {
		log.info("Get booking with state {}, ownerId={}, from={}, size={}", state, userId, from, size);
		return bookingClient.getBookingsByOwner(userId, state, from, size, sortBy, order);
	}
}
