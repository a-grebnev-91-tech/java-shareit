package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.domain.BookingsState;
import ru.practicum.shareit.exception.BookingStateIsNotSupportedException;
import ru.practicum.shareit.requests.repository.OffsetPageable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class BookingParamObj {
    public static final String BOOKING_DEFAULT_SORT_BY = "start";
    public static final String BOOKING_DEFAULT_ORDER = "DESC";
    public static final String BOOKING_DEFAULT_STATE = "ALL";
    public static final int BOOKING_DEFAULT_OFFSET = 0;
    public static final int BOOKING_DEFAULT_SIZE = 20;

    private final Long userId;
    private final BookingsState state;
    private final Pageable pageable;

    private BookingParamObj(BookingParamBuilder builder) {
        this.userId = builder.userId;
        this.state = builder.state == null ?
                convertToBookingState(BOOKING_DEFAULT_STATE) : convertToBookingState(builder.state);
        String sortBy = builder.sortBy == null ? BOOKING_DEFAULT_SORT_BY : builder.sortBy;
        String sortOrder = builder.sortOrder == null ? BOOKING_DEFAULT_ORDER : builder.sortOrder;
        int offset = builder.from == null ? BOOKING_DEFAULT_OFFSET : builder.from;
        int size = builder.size == null ? BOOKING_DEFAULT_SIZE : builder.size;
        Sort sort = Sort.by(Sort.Direction.valueOf(sortOrder), sortBy);
        this.pageable = OffsetPageable.of(offset, size, sort);
    }

    private static BookingsState convertToBookingState(String state) {
        try {
            return BookingsState.valueOf(state);
        } catch (IllegalArgumentException ex) {
            throw new BookingStateIsNotSupportedException(String.format("Unknown state: %s", state));
        }
    }

    public static BookingParamBuilder newBuilder() {
        return new BookingParamBuilder();
    }

    public static final class BookingParamBuilder {
        private Long userId;
        private String state;
        private String sortBy;
        private String sortOrder;
        private Integer from;
        private Integer size;

        private BookingParamBuilder() {
        }

        public BookingParamBuilder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public BookingParamBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public BookingParamBuilder from(Integer from) {
            this.from = from;
            return this;
        }

        public BookingParamBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public BookingParamBuilder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public BookingParamBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public BookingParamObj build() {
            return new BookingParamObj(this);
        }
    }
}
