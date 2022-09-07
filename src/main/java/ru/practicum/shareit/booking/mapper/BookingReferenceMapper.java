package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.controller.dto.LastBookingDto;
import ru.practicum.shareit.booking.controller.dto.NextBookingDto;
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

    public LastBookingDto mapToLast(Long itemId) {
        List<Booking> booking = repo.findByItemId(itemId);
        TreeSet<Booking> bookingTreeSet = new TreeSet<>((b1, b2) -> b1.getEnd().compareTo(b2.getEnd()));
        bookingTreeSet.addAll(booking);
        Booking nowBooking = new Booking();
        nowBooking.setEnd(LocalDateTime.now());
        Booking lastBooking = bookingTreeSet.floor(nowBooking);
        if (lastBooking == null) {
            return null;
        }
        LastBookingDto result = new LastBookingDto();
        mapper.updateBookingForItemFromBooking(lastBooking, result);
        return result;
    }

    public NextBookingDto mapToNext(Long itemId) {
        List<Booking> booking = repo.findByItemId(itemId);
        TreeSet<Booking> bookingTreeSet = new TreeSet<>((b1, b2) -> b1.getStart().compareTo(b2.getStart()));
        bookingTreeSet.addAll(booking);
        Booking nowBooking = new Booking();
        nowBooking.setStart(LocalDateTime.now());
        Booking nextBooking = bookingTreeSet.ceiling(nowBooking);
        if (nextBooking == null) {
            return null;
        }
        NextBookingDto result = new NextBookingDto();
        mapper.updateBookingForItemFromBooking(nextBooking, result);
        return result;
    }
}
