package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaBookingRepository extends JpaRepository<Booking, Long>, BookingRepository {

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.booker.id = ?1 AND b.item.id = ?2 AND b.end < ?3")
    List<Booking> findAllByBookerIdAndItemIdAndEndDateInPast(Long bookerId, Long itemId, LocalDateTime now);

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(Long ownerId);

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdAndStatus(Long ownerId, BookingStatus status);

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start < ?2 AND b.end > ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdCurrent(Long ownerId, LocalDateTime now);

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdInFuture(Long ownerId, LocalDateTime now);

    @Override
    @Query("SELECT b FROM Booking AS b WHERE b.item.owner.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdInPast(Long ownerId, LocalDateTime now);
}
