package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    List<Booking> findAllByBookerId(Long userId, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByOwnerAndStatus(Long ownerId, BookingStatus status);

    List<Booking> findAllByOwnerId(Long ownerId);

    List<Booking> findAllComingByBooker(Long bookerId, Sort sort);

    List<Booking> findAllComingByOwner(Long ownerId);

    List<Booking> findAllCompletedByBookerAndItem(Long bookerId, Long itemId);

    List<Booking> findAllCurrentByBooker(Long bookerId, Sort sort);

    List<Booking> findAllCurrentByOwner(Long ownerId);

    List<Booking> findAllPastByBooker(Long bookerId, Sort sort);

    List<Booking> findAllPastByOwner(Long ownerId);

    Optional<Booking> findById(Long bookingId);

    List<Booking> findByItemId(Long itemId);

    Booking save(Booking booking);
}
