package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.controller.dto.BookingInputDto;
import ru.practicum.shareit.booking.controller.dto.BookingOutputDto;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.mapper.ItemReferenceMapper;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingMapperTest {
    private static final Long BOOKING_ID = 1L;
    private static final Long ITEM_ID = 2L;
    private static final Long USER_ID = 3L;
    private static final LocalDateTime START = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime END = LocalDateTime.now().plusDays(2);
    @Mock
    private UserReferenceMapper userRefMap;
    @Mock
    private ItemReferenceMapper itemRefMap;
    @InjectMocks
    private BookingMapperImpl mapper;
    private BookingInputDto inDto;
    private Booking model;
    private BookingOutputDto outDto;
    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        inDto = new BookingInputDto();
        inDto.setItemId(ITEM_ID);
        inDto.setStart(START);
        inDto.setEnd(END);

        item = new Item();
        item.setId(ITEM_ID);

        user = new User();
        user.setId(USER_ID);

        model = new Booking();
        model.setId(BOOKING_ID);
        model.setStart(START);
        model.setEnd(END);
        model.setItem(item);
        model.setBooker(user);

        outDto = new BookingOutputDto();
        outDto.setId(BOOKING_ID);
        outDto.setStart(START);
        outDto.setEnd(END);
    }

    @Test
    void test1_dtoToModel() {
        when(itemRefMap.idToItem(ITEM_ID)).thenReturn(item);
        when(userRefMap.idToUser(USER_ID)).thenReturn(user);

        Booking fromMapper = mapper.dtoToModel(inDto, USER_ID);
        assertNotNull(fromMapper);
        assertEquals(START, fromMapper.getStart());
        assertEquals(END, fromMapper.getEnd());
        assertEquals(USER_ID, fromMapper.getBooker().getId());
        assertEquals(ITEM_ID, fromMapper.getItem().getId());
    }

    @Test
    void test2_modelToResponse() {
        BookingOutputDto fromMapper = mapper.modelToResponse(model);
        assertNotNull(fromMapper);
        assertEquals(START, fromMapper.getStart());
        assertEquals(END, fromMapper.getEnd());
        assertEquals(USER_ID, fromMapper.getBooker().getId());
        assertEquals(ITEM_ID, fromMapper.getItem().getId());
    }

    @Test
    void test3_modelToListResponse() {
        List<BookingOutputDto> fromMapper = mapper.modelsToResponse(List.of(model));
        assertNotNull(fromMapper);
        assertEquals(1, fromMapper.size());
        assertEquals(START, fromMapper.get(0).getStart());
        assertEquals(END, fromMapper.get(0).getEnd());
        assertEquals(USER_ID, fromMapper.get(0).getBooker().getId());
        assertEquals(ITEM_ID, fromMapper.get(0).getItem().getId());
    }
}