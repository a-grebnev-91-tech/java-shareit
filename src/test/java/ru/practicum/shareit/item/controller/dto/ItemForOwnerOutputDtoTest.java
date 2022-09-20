package ru.practicum.shareit.item.controller.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.controller.dto.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemForOwnerOutputDtoTest {
    @Autowired
    JacksonTester<ItemForOwnerOutputDto> json;
    private Long id = 1L;
    private String name = "name";
    private String description = "descr";
    private Boolean available = true;
    private Long requestId = 2L;
    private Long commentsId = 3L;
    private String commentsText = "text";
    private String commentsAuthorName = "author";
    private LocalDateTime commentsCreated = LocalDateTime.now().minusDays(1);
    private Long lastBookingId = 4L;
    private Long lastBookingBookerId = 5L;
    private LocalDateTime lastBookingStart = LocalDateTime.now().minusHours(2);
    private LocalDateTime lastBookingEnd = LocalDateTime.now().minusHours(1);
    private Long nextBookingId = 6L;
    private Long nextBookingBookerId = 7L;
    private LocalDateTime nextBookingStart = LocalDateTime.now().plusHours(1);
    private LocalDateTime nextBookingEnd = LocalDateTime.now().plusDays(2);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Test
    void test1_serialize() throws IOException {
        ItemForOwnerOutputDto dto = getDto();

        JsonContent<ItemForOwnerOutputDto> result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).hasJsonPath("$.name");
        assertThat(result)
                .extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).hasJsonPath("$.description");
        assertThat(result)
                .extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).hasJsonPath("$.available");
        assertThat(result)
                .extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).hasJsonPath("$.requestId");
        assertThat(result)
                .extractingJsonPathNumberValue("$.requestId").isEqualTo(dto.getRequestId().intValue());
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).hasJsonPath("$.comments[0].id");
        assertThat(result)
                .extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(dto.getComments().get(0).getId().intValue());
        assertThat(result).hasJsonPath("$.comments[0].text");
        assertThat(result)
                .extractingJsonPathStringValue("$.comments[0].text")
                .isEqualTo(dto.getComments().get(0).getText());
        assertThat(result).hasJsonPath("$.comments[0].created");
        assertThat(result)
                .extractingJsonPathStringValue("$.comments[0].created")
                .startsWith(dto.getComments().get(0).getCreated().format(formatter));
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.lastBooking.id");
        assertThat(result)
                .extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(dto.getLastBooking().getId().intValue());
        assertThat(result).hasJsonPath("$.lastBooking.bookerId");
        assertThat(result)
                .extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(dto.getLastBooking().getBookerId().intValue());
        assertThat(result).hasJsonPath("$.lastBooking.start");
        assertThat(result)
                .extractingJsonPathStringValue("$.lastBooking.start")
                .startsWith(dto.getLastBooking().getStart().format(formatter));
        assertThat(result).hasJsonPath("$.lastBooking.end");
        assertThat(result)
                .extractingJsonPathStringValue("$.lastBooking.end")
                .startsWith(dto.getLastBooking().getEnd().format(formatter));
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.nextBooking.id");
        assertThat(result)
                .extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(dto.getNextBooking().getId().intValue());
        assertThat(result).hasJsonPath("$.nextBooking.bookerId");
        assertThat(result)
                .extractingJsonPathNumberValue("$.nextBooking.bookerId")
                .isEqualTo(dto.getNextBooking().getBookerId().intValue());
        assertThat(result).hasJsonPath("$.nextBooking.start");
        assertThat(result)
                .extractingJsonPathStringValue("$.nextBooking.start")
                .startsWith(dto.getNextBooking().getStart().format(formatter));
        assertThat(result).hasJsonPath("$.nextBooking.end");
        assertThat(result)
                .extractingJsonPathStringValue("$.nextBooking.end")
                .startsWith(dto.getNextBooking().getEnd().format(formatter));
    }

    private ItemForOwnerOutputDto getDto() {
        CommentOutputDto comDto = new CommentOutputDto();
        comDto.setId(commentsId);
        comDto.setText(commentsText);
        comDto.setAuthorName(commentsAuthorName);
        comDto.setCreated(commentsCreated);
        List<CommentOutputDto> comments = new ArrayList<>();
        comments.add(comDto);

        BookingForItemDto lastBookDto = new BookingForItemDto();
        lastBookDto.setId(lastBookingId);
        lastBookDto.setBookerId(lastBookingBookerId);
        lastBookDto.setStart(lastBookingStart);
        lastBookDto.setEnd(lastBookingEnd);

        BookingForItemDto nextBookDto = new BookingForItemDto();
        nextBookDto.setId(nextBookingId);
        nextBookDto.setBookerId(nextBookingBookerId);
        nextBookDto.setStart(nextBookingStart);
        nextBookDto.setEnd(nextBookingEnd);

        ClosestBookings closestBookings = new ClosestBookings(lastBookDto, nextBookDto);

        ItemForOwnerOutputDto dto = new ItemForOwnerOutputDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(available);
        dto.setRequestId(requestId);
        dto.setClosestBookings(closestBookings);
        dto.setComments(comments);

        return dto;
    }

}