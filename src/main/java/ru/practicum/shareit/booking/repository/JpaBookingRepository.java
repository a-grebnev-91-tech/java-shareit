package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaBookingRepository extends JpaRepository<Booking, Long>, BookingRepository {
    @Override
    default List<Booking> findAllComingByBooker(Long bookerId, Pageable pageable) {
        return findAllByBookerIdAndStartIsAfter(bookerId, LocalDateTime.now(), pageable);
    }

    @Override
    @Query(value = "SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.status = ?2")
    List<Booking> findAllByOwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1")
    List<Booking> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Override
    default List<Booking> findAllComingByOwnerId(Long ownerId, Pageable pageable) {
        return findAllComingByOwner(ownerId, LocalDateTime.now(), pageable);
    }

    @Override
    default List<Booking> findAllCompletedByBookerAndItem(Long bookerId, Long itemId) {
        return findAllCompletedByBookerAndItem(bookerId, itemId, LocalDateTime.now());
    }

    @Override
    default List<Booking> findAllCurrentByBooker(Long bookerId, Pageable pageable) {
        return findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
                bookerId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                pageable
        );
    }

    @Override
    default List<Booking> findAllCurrentByOwnerId(Long ownerId, Pageable pageable) {
        return findAllCurrentByOwner(ownerId, LocalDateTime.now(), pageable);
    }

    @Override
    default List<Booking> findAllPastByBooker(Long bookerId, Pageable pageable) {
        return findAllByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now(), pageable);
    }

    @Override
    default List<Booking> findAllPastByOwnerId(Long ownerId, Pageable pageable) {
        return findAllPastByOwner(ownerId, LocalDateTime.now(), pageable);
    }

    List<Booking> findAllByBookerIdAndEndIsBefore(Long id, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId,
            LocalDateTime nowForStart,
            LocalDateTime nowForEnd,
            Pageable pageable
    );

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start > ?2")
    List<Booking> findAllComingByOwner(Long ownerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b WHERE b.booker.id = ?1 AND b.item.id = ?2 AND b.end < ?3")
    List<Booking> findAllCompletedByBookerAndItem(Long bookerId, Long itemId, LocalDateTime now);

    @Query(value = "SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start < ?2 AND b.end > ?2")
    List<Booking> findAllCurrentByOwner(Long ownerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.end < ?2")
    List<Booking> findAllPastByOwner(Long ownerId, LocalDateTime now, Pageable pageable);
}
