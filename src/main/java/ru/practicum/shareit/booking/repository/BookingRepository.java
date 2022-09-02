package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    List<Booking> findAllByBookerId(Long userId, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBefore(Long id, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId,
            LocalDateTime nowForStart,
            LocalDateTime nowForEnd,
            Sort sort
    );

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByOwnerId(Long ownerId);

    List<Booking> findAllByOwnerIdAndStatus(Long ownerId, BookingStatus status);

    List<Booking> findAllByOwnerIdCurrent(Long ownerId, LocalDateTime now);

    List<Booking> findAllByOwnerIdInFuture(Long ownerId, LocalDateTime now);

    List<Booking> findAllByOwnerIdInPast(Long ownerId, LocalDateTime now);

    Optional<Booking> findById(Long bookingId);

    List<Booking> findByItemId(Long itemId);

    Booking save(Booking booking);
}
