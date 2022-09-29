package ru.practicum.shareit.booking.controller.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingOutputDtoTest {
    @Autowired
    JacksonTester<BookingOutputDto> json;
    Long id = 1L;
    Long bookerId = 2L;
    Long itemId = 3L;
    String itemName = "item";
    String status = "APPROVED";
    LocalDateTime start = LocalDateTime.now().plusDays(1);
    LocalDateTime end = LocalDateTime.now().plusDays(2);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Test
    void test1_serialize() throws IOException {
        BookingOutputDto dto = getDto();

        JsonContent<BookingOutputDto> result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result)
                .extractingJsonPathNumberValue("$.booker.id").isEqualTo(dto.getBooker().getId().intValue());
        assertThat(result)
                .extractingJsonPathNumberValue("$.item.id").isEqualTo(dto.getItem().getId().intValue());
        assertThat(result)
                .extractingJsonPathStringValue("$.item.name").isEqualTo(dto.getItem().getName());
        assertThat(result)
                .extractingJsonPathStringValue("$.status").isEqualTo(dto.getStatus());
        assertThat(result)
                .extractingJsonPathStringValue("$.start").startsWith(dto.getStart().format(formatter));
        assertThat(result)
                .extractingJsonPathStringValue("$.end").startsWith(dto.getEnd().format(formatter));
    }

    private BookingOutputDto getDto() {
        BookerDto booker = new BookerDto();
        booker.setId(bookerId);

        BookedItemDto item = new BookedItemDto();
        item.setId(itemId);
        item.setName(itemName);

        BookingOutputDto dto = new BookingOutputDto();
        dto.setId(id);
        dto.setBooker(booker);
        dto.setItem(item);
        dto.setStatus(status);
        dto.setStart(start);
        dto.setEnd(end);
        return dto;
    }
}