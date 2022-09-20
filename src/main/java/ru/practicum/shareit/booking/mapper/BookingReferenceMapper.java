package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.controller.dto.BookingForItemDto;
import ru.practicum.shareit.booking.controller.dto.ClosestBookings;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

@Component
@RequiredArgsConstructor
public class BookingReferenceMapper {
    private final BookingRepository repo;
    private final BookingMapper mapper;

    public ClosestBookings itemIdToClosestBooking(Long itemId) {
        List<Booking> bookings = repo.findByAvailableItem(itemId);
        TreeSet<Booking> bookingsByEnd = new TreeSet<>((b1, b2) -> b1.getEnd().compareTo(b2.getEnd()));
        TreeSet<Booking> bookingsByStart = new TreeSet<>((b1, b2) -> b1.getStart().compareTo(b2.getStart()));
        bookingsByEnd.addAll(bookings);
        bookingsByStart.addAll(bookings);
        Booking nowBooking = new Booking();
        nowBooking.setStart(LocalDateTime.now());
        nowBooking.setEnd(LocalDateTime.now());
        Booking lastBooking = bookingsByEnd.floor(nowBooking);
        Booking nextBooking = bookingsByStart.ceiling(nowBooking);
        BookingForItemDto lastDto = lastBooking == null ? null : mapper.modelToForItemDto(lastBooking);
        BookingForItemDto nextDto = nextBooking == null ? null : mapper.modelToForItemDto(nextBooking);
        return new ClosestBookings(lastDto, nextDto);
    }
}
