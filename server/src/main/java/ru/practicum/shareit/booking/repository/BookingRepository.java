package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    List<Booking> findAllByBookerId(Long userId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByOwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findAllComingByBooker(Long bookerId, Pageable pageable);

    List<Booking> findAllComingByOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findAllCompletedByBookerAndItem(Long bookerId, Long itemId);

    List<Booking> findAllCurrentByBooker(Long bookerId, Pageable pageable);

    List<Booking> findAllCurrentByOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findAllPastByBooker(Long bookerId, Pageable pageable);

    List<Booking> findAllPastByOwnerId(Long ownerId, Pageable pageable);

    Optional<Booking> findById(Long bookingId);

    List<Booking> findByAvailableItem(Long itemId);

    Booking save(Booking booking);
}
