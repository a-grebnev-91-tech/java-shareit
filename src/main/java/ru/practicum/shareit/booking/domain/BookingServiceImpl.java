package ru.practicum.shareit.booking.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingParamObj;
import ru.practicum.shareit.booking.controller.dto.BookingInputDto;
import ru.practicum.shareit.booking.controller.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository bookingRepository;
    @Qualifier("InDbUsers")
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingOutputDto approveBooking(Long bookingId, Long userId, boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);
        if (isBooker(userId, booking) && approved)
            //TODO исправить после завершения проекта
            // не знаю, почему здесь NotFound, но такой код ответа требуют тесты постмана
            throw new NotFoundException("Booker couldn't approve his booking");
        if (isOwner(userId, booking) && booking.getStatus() == BookingStatus.WAITING) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            return mapper.modelToResponse(bookingRepository.save(booking));
        } else {
            throw new NotAvailableException("Could not change booking status");
        }
    }

    @Override
    public BookingOutputDto createBooking(BookingInputDto bookingInputDto, Long userId) {
        Booking booking = mapper.dtoToModel(bookingInputDto, userId);
        if (isOwner(userId, booking))
            //TODO исправить после завершения проекта
            // не знаю, почему здесь NotFound, но такой код ответа требуют тесты постмана
            throw new NotFoundException("The user can't book his own item");
        Item requestedItem = booking.getItem();
        if (requestedItem.isAvailable()) {
            return mapper.modelToResponse(bookingRepository.save(booking));
        } else {
            throw new NotAvailableException(String.format("Item with id %d isn't available", requestedItem.getId()));
        }
    }

    @Override
    public List<BookingOutputDto> getAllBookingsByBooker(BookingParamObj paramObj) {
        checkUserExisting(paramObj.getUserId());
        List<Booking> bookings = null;
        switch (paramObj.getState()) {
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(
                        paramObj.getUserId(),
                        BookingStatus.valueOf(paramObj.getState().name()),
                        paramObj.getPageable()
                );
                break;
            case ALL:
                bookings = bookingRepository.findAllByBookerId(paramObj.getUserId(), paramObj.getPageable());
                break;
            case PAST:
                bookings = bookingRepository.findAllPastByBooker(paramObj.getUserId(), paramObj.getPageable());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllCurrentByBooker(paramObj.getUserId(), paramObj.getPageable());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllComingByBooker(paramObj.getUserId(), paramObj.getPageable());
                break;
        }
        return mapper.modelsToResponse(bookings);
    }

    @Override
    public List<BookingOutputDto> getAllBookingsByOwner(BookingParamObj paramObj) {
        checkUserExisting(paramObj.getUserId());
        List<Booking> bookings = null;
        switch (paramObj.getState()) {
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(
                        paramObj.getUserId(),
                        BookingStatus.valueOf(paramObj.getState().name()),
                        paramObj.getPageable()
                );
                break;
            case ALL:
                bookings = bookingRepository.findAllByOwnerId(paramObj.getUserId(), paramObj.getPageable());
                break;
            case PAST:
                bookings = bookingRepository.findAllPastByOwnerId(paramObj.getUserId(), paramObj.getPageable());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllCurrentByOwnerId(paramObj.getUserId(), paramObj.getPageable());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllComingByOwnerId(paramObj.getUserId(), paramObj.getPageable());
                break;
        }
        return mapper.modelsToResponse(bookings);
    }

    @Override
    public BookingOutputDto getBooking(Long bookingId, Long userId) {
        Booking booking = getBookingOrThrow(bookingId);
        if (isBooker(userId, booking) || isOwner(userId, booking)) {
            return mapper.modelToResponse(booking);
        } else {
            //TODO исправить по завершению проекта на NotAvailable
            throw new NotFoundException(
                    String.format("User with id %d doesn't have access to booking with id %d", userId, bookingId)
            );
        }
    }

    private void checkUserExisting(Long userId) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException(String.format("User with id %d isn't exist", userId));
    }

    private Booking getBookingOrThrow(Long bookingId) {
        if (bookingId == null) throw new NotAvailableException("Booking id should not be null");
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.orElseThrow(
                () -> new NotFoundException(String.format("Booking with id %d isn't exist", bookingId))
        );
    }

    private boolean isBooker(Long userId, Booking booking) {
        if (userId == null) return false;
        checkUserExisting(userId);
        return userId.equals(booking.getBooker().getId());
    }

    private boolean isOwner(Long userId, Booking booking) {
        if (userId == null) return false;
        checkUserExisting(userId);
        return userId.equals(booking.getItem().getOwner().getId());
    }
}
