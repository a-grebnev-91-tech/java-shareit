package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);

    List<Booking> findAllByBookerId(Long userId, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBefore(Long id, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId,
            LocalDateTime nowStart,
            LocalDateTime nowEnd,
            Sort sort
    );

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    Optional<Booking> findById(Long bookingId);
}
