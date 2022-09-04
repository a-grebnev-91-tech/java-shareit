package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
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
    default List<Booking> findAllComingByBooker(Long bookerId, Sort sort) {
        return findAllByBookerIdAndStartIsAfter(bookerId, LocalDateTime.now(), sort);
    }

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerAndStatus(Long ownerId, BookingStatus status);

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(Long ownerId);

    @Override
    default List<Booking> findAllComingByOwner(Long ownerId) {
        return findAllComingByOwner(ownerId, LocalDateTime.now());
    }

    @Override
    default List<Booking> findAllCompletedByBookerAndItem(Long bookerId, Long itemId) {
        return findAllCompletedByBookerAndItem(bookerId, itemId, LocalDateTime.now());
    }

    @Override
    default List<Booking> findAllCurrentByBooker(Long bookerId, Sort sort) {
        return findAllByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId, LocalDateTime.now(), LocalDateTime.now(), sort);
    }

    @Override
    default List<Booking> findAllCurrentByOwner(Long ownerId) {
        return findAllCurrentByOwner(ownerId, LocalDateTime.now());
    }

    @Override
    default List<Booking> findAllPastByBooker(Long bookerId, Sort sort) {
        return findAllByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now(), sort);
    }

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    default List<Booking> findAllPastByOwner(Long ownerId) {
        return findAllPastByOwner(ownerId, LocalDateTime.now());
    }

    List<Booking> findAllByBookerIdAndEndIsBefore(Long id, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId,
            LocalDateTime nowForStart,
            LocalDateTime nowForEnd,
            Sort sort
    );

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> findAllComingByOwner(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking AS b WHERE b.booker.id = ?1 AND b.item.id = ?2 AND b.end < ?3")
    List<Booking> findAllCompletedByBookerAndItem(Long bookerId, Long itemId, LocalDateTime now);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start < ?2 AND b.end > ?2 ORDER BY b.start DESC")
    List<Booking> findAllCurrentByOwner(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> findAllPastByOwner(Long ownerId, LocalDateTime now);
}
