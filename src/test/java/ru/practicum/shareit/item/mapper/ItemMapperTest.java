package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.controller.dto.BookingForItemDto;
import ru.practicum.shareit.booking.controller.dto.ClosestBookings;
import ru.practicum.shareit.booking.mapper.BookingReferenceMapper;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemForOwnerOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemInputDto;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.item.domain.Comment;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.requests.domain.Request;
import ru.practicum.shareit.requests.mapper.RequestReferenceMapper;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {
    private static final Long OWNER_ID = 1L;
    private static final Long AUTHOR_ID = 2L;
    private static final String AUTHOR_NAME = "Name";
    private static final Long ITEM_ID = 3L;
    private static final Long COMMENT_ID = 4L;
    private static final String COMMENT_TEXT = "Comment text";
    private static final String ITEM_NAME = "Item name";
    private static final String ITEM_DESCRIPTION = "Item descr";
    private static final Boolean ITEM_AVAILABLE = true;
    private static final Long NEXT_BOOKING_ID = 5L;
    private static final Long LAST_BOOKING_ID = 6L;
    private static final Long REQUEST_ID = 7L;

    @Mock
    private UserReferenceMapper userReferenceMapper;
    @Mock
    private BookingReferenceMapper bookingReferenceMapper;
    @Mock
    private CommentReferenceMapper commentReferenceMapper;
    @Mock
    private RequestReferenceMapper requestReferenceMapper;
    @InjectMocks
    private ItemMapperImpl mapper;

    private User owner;
    private User author;
    private ItemInputDto inDto;
    private Item model;
    private ItemForOwnerOutputDto ownerOutDto;
    private ItemOutputDto outDto;
    private BookingForItemDto lastBookDto;
    private BookingForItemDto nextBookDto;
    private Comment comment;
    private CommentOutputDto commentOutDto;
    private Request request;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(OWNER_ID);

        author = new User();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);

        model = new Item();
        model.setId(ITEM_ID);
        model.setOwner(owner);
        model.setName(ITEM_NAME);
        model.setDescription(ITEM_DESCRIPTION);
        model.setAvailable(ITEM_AVAILABLE);
        model.setRequest(request);

        inDto = new ItemInputDto();
        inDto.setName(ITEM_NAME);
        inDto.setDescription(ITEM_DESCRIPTION);
        inDto.setAvailable(ITEM_AVAILABLE);

        nextBookDto = new BookingForItemDto();
        nextBookDto.setId(NEXT_BOOKING_ID);

        lastBookDto = new BookingForItemDto();
        lastBookDto.setId(LAST_BOOKING_ID);

        commentOutDto = new CommentOutputDto();
        commentOutDto.setId(COMMENT_ID);
        commentOutDto.setText(COMMENT_TEXT);
        commentOutDto.setAuthorName(AUTHOR_NAME);

        ownerOutDto = new ItemForOwnerOutputDto();
        ownerOutDto.setId(ITEM_ID);
        ownerOutDto.setName(ITEM_NAME);
        ownerOutDto.setDescription(ITEM_DESCRIPTION);
        ownerOutDto.setAvailable(ITEM_AVAILABLE);
        ownerOutDto.setComments(List.of(commentOutDto));
        ownerOutDto.setNextBooking(nextBookDto);
        ownerOutDto.setLastBooking(lastBookDto);

        comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setItem(model);
        comment.setText(COMMENT_TEXT);
        comment.setAuthor(author);

        request = new Request();
        request.setId(REQUEST_ID);

        outDto = new ItemOutputDto();
        outDto.setId(ITEM_ID);
        outDto.setName(ITEM_NAME);
        outDto.setDescription(ITEM_DESCRIPTION);
        outDto.setAvailable(ITEM_AVAILABLE);
        outDto.setComments(List.of(commentOutDto));
    }

    @Test
    void test1_toModel() {
        when(userReferenceMapper.idToUser(OWNER_ID)).thenReturn(owner);

        Item fromMapper = mapper.toModel(inDto, OWNER_ID);

        assertNotNull(fromMapper);
        assertEquals(ITEM_NAME, fromMapper.getName());
        assertEquals(ITEM_DESCRIPTION, fromMapper.getDescription());
        assertEquals(ITEM_AVAILABLE, fromMapper.isAvailable());
        assertEquals(OWNER_ID, fromMapper.getOwner().getId());
    }

    @Test
    void test2_toOwnerResponse() {
        ClosestBookings closestBookings = new ClosestBookings(lastBookDto, nextBookDto);
        when(bookingReferenceMapper.itemIdToClosestBooking(ITEM_ID)).thenReturn(closestBookings);
        when(commentReferenceMapper.map(ITEM_ID)).thenReturn(List.of(commentOutDto));

        ItemForOwnerOutputDto fromMapper = mapper.toOwnerResponse(model);

        assertNotNull(fromMapper);
        assertEquals(ITEM_ID, fromMapper.getId());
        assertEquals(ITEM_NAME, fromMapper.getName());
        assertEquals(ITEM_DESCRIPTION, fromMapper.getDescription());
        assertEquals(ITEM_AVAILABLE, fromMapper.getAvailable());
        assertEquals(NEXT_BOOKING_ID, fromMapper.getNextBooking().getId());
        assertEquals(LAST_BOOKING_ID, fromMapper.getLastBooking().getId());
        assertEquals(1, fromMapper.getComments().size());
        assertEquals(COMMENT_ID, fromMapper.getComments().get(0).getId());
    }

    @Test
    void test3_toResponse() {
        when(commentReferenceMapper.map(ITEM_ID)).thenReturn(List.of(commentOutDto));
        when(requestReferenceMapper.requestToId(any())).thenReturn(REQUEST_ID);

        ItemOutputDto fromMapper = mapper.toResponse(model);

        assertNotNull(fromMapper);
        assertEquals(ITEM_ID, fromMapper.getId());
        assertEquals(ITEM_NAME, fromMapper.getName());
        assertEquals(ITEM_DESCRIPTION, fromMapper.getDescription());
        assertEquals(ITEM_AVAILABLE, fromMapper.getAvailable());
        assertEquals(1, fromMapper.getComments().size());
        assertEquals(COMMENT_ID, fromMapper.getComments().get(0).getId());
        assertEquals(REQUEST_ID, fromMapper.getRequestId());
    }
}