package ru.practicum.shareit.exception;

public class BookingStateIsNotSupportedException extends RuntimeException {
    public BookingStateIsNotSupportedException(String message) {
        super(message);
    }
}
