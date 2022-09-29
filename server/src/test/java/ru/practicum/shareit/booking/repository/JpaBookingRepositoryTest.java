package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingStatus;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class JpaBookingRepositoryTest {
    private static final LocalDateTime START_PAST = LocalDateTime.now().minusDays(2);
    private static final LocalDateTime END_PAST = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime START_FUTURE = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime END_FUTURE = LocalDateTime.now().plusDays(2);
    private static final Pageable page = Pageable.ofSize(10);
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepository repository;

    @Test
    void test1_findAllComingByBooker() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking futureBooking = getBookingInFuture(item, booker);
        Booking pastBooking = getBookingInPast(item, booker);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(futureBooking);
        em.persist(pastBooking);

        List<Booking> bookings = repository.findAllComingByBooker(booker.getId(), page);
        assertThat(bookings).hasSize(1).contains(futureBooking);
    }

    @Test
    void test2_findAllByOwnerIdAndStatus() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking waitingBooking = getBookingInFuture(item, booker);
        Booking canceledBooking = getBookingInPast(item, booker);
        canceledBooking.setStatus(BookingStatus.CANCELED);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(waitingBooking);
        em.persist(canceledBooking);

        List<Booking> waitingBookings = repository.findAllByOwnerIdAndStatus(owner.getId(), BookingStatus.WAITING, page);
        assertThat(waitingBookings).hasSize(1).contains(waitingBooking);

        List<Booking> canceledBookings = repository.findAllByOwnerIdAndStatus(owner.getId(), BookingStatus.CANCELED, page);
        assertThat(canceledBookings).hasSize(1).contains(canceledBooking);
    }

    @Test
    void test3_findAllComingByOwnerId() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking futureBooking = getBookingInFuture(item, booker);
        Booking pastBooking = getBookingInPast(item, booker);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(futureBooking);
        em.persist(pastBooking);

        List<Booking> bookings = repository.findAllComingByOwnerId(owner.getId(), page);
        assertThat(bookings).hasSize(1).contains(futureBooking);
    }

    @Test
    void test4_findAllCompletedByBookerAndItem() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking futureBooking = getBookingInFuture(item, booker);
        Booking pastBooking = getBookingInPast(item, booker);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(futureBooking);
        em.persist(pastBooking);

        List<Booking> bookings = repository.findAllCompletedByBookerAndItem(booker.getId(), item.getId());
        assertThat(bookings).hasSize(1).contains(pastBooking);
    }

    @Test
    void test5_findAllCurrentByBooker() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking futureBooking = getBookingInFuture(item, booker);
        Booking currentBooking = getBookingInFuture(item, booker);
        currentBooking.setStart(START_PAST);
        Booking pastBooking = getBookingInPast(item, booker);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(futureBooking);
        em.persist(currentBooking);
        em.persist(pastBooking);

        List<Booking> bookings = repository.findAllCurrentByBooker(booker.getId(), page);
        assertThat(bookings).hasSize(1).contains(currentBooking);
    }

    @Test
    void test6_findAllCurrentByOwnerId() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking futureBooking = getBookingInFuture(item, booker);
        Booking currentBooking = getBookingInFuture(item, booker);
        currentBooking.setStart(START_PAST);
        Booking pastBooking = getBookingInPast(item, booker);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(futureBooking);
        em.persist(currentBooking);
        em.persist(pastBooking);

        List<Booking> bookings = repository.findAllCurrentByOwnerId(owner.getId(), page);
        assertThat(bookings).hasSize(1).contains(currentBooking);
    }

    @Test
    void test7_findAllPastByBooker() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking futureBooking = getBookingInFuture(item, booker);
        Booking pastBooking = getBookingInPast(item, booker);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(futureBooking);
        em.persist(pastBooking);

        List<Booking> bookings = repository.findAllPastByBooker(booker.getId(), page);
        assertThat(bookings).hasSize(1).contains(pastBooking);
    }

    @Test
    void test8_findAllPastByOwnerId() {
        User owner = getUser();
        Item item = getItem(owner);
        User booker = getUser();
        Booking futureBooking = getBookingInFuture(item, booker);
        Booking pastBooking = getBookingInPast(item, booker);
        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(futureBooking);
        em.persist(pastBooking);

        List<Booking> bookings = repository.findAllPastByOwnerId(owner.getId(), page);
        assertThat(bookings).hasSize(1).contains(pastBooking);
    }

    private Booking getBookingInFuture(Item item, User booker) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(START_FUTURE);
        booking.setEnd(END_FUTURE);
        return booking;
    }

    private Booking getBookingInPast(Item item, User booker) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(START_PAST);
        booking.setEnd(END_PAST);
        return booking;
    }

    private Item getItem(User owner) {
        Item item = new Item();
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(owner);
        return item;
    }

    private User getUser() {
        User user = new User();
        user.setName("user-puser");
        user.setEmail(generateRandomEmail(7));
        return user;
    }

    private String generateRandomEmail(int len) {
        Random rnd = new Random();
        StringBuilder mailBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = rnd.nextInt(25);
            char c = (char) (97 + index);
            mailBuilder.append(c);
        }
        mailBuilder.append("@mail.com");
        return mailBuilder.toString();
    }
}