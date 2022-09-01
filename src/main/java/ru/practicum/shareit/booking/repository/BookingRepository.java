package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Optional<Booking> findById(Long bookingId);

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

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(Long ownerId);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStatus(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start < ?2 AND b.end > ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdCurrent(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdInFuture(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdInPast(Long ownerId, LocalDateTime now);

    Booking save(Booking booking);
}
