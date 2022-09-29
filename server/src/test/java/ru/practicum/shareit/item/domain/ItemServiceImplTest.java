package ru.practicum.shareit.item.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.controller.dto.BookingForItemDto;
import ru.practicum.shareit.booking.controller.dto.ClosestBookings;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PatchException;
import ru.practicum.shareit.item.ItemsParamObject;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Patcher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 2L;
    private static final Long COMMENT_ID = 3L;
    private static final String COMMENT_TEXT = "comment text";
    private static final Long OUTSIDE_USER_ID = 4L;
    @Mock
    private BookingRepository bookRepo;
    @Mock
    private CommentRepository comRepo;
    @Mock
    private ItemRepository itemRepo;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private Patcher patcher;
    @Mock
    private UserRepository userRepo = Mockito.mock(UserRepository.class);
    @Mock
    private CommentMapper comMapper;
    @InjectMocks
    private ItemServiceImpl service;
    private CommentInputDto inDto;
    private Comment comment;
    private CommentOutputDto outDto;
    private User user;
    private User outsideUser;
    private Item item;
    private ItemsParamObject paramObject;
    private ItemForOwnerOutputDto itemOutOwnerDto;
    private ItemInputDto patch;

    @BeforeEach
    void setUp() {
        setUpCommentLayer();
        paramObject = ItemsParamObject.newBuilder().withUserId(USER_ID).withText("text").build();
        setUpUserLayer();
        setUpItemLayer();
    }

    private void setUpItemLayer() {
        item = new Item();
        item.setOwner(user);
        patch = new ItemInputDto();
        patch.setDescription("patch descr");
        itemOutOwnerDto = new ItemForOwnerOutputDto();
        itemOutOwnerDto.setId(ITEM_ID);
        itemOutOwnerDto.setClosestBookings(new ClosestBookings(new BookingForItemDto(), new BookingForItemDto()));
        itemOutOwnerDto.setDescription(patch.getDescription());
        when(itemRepo.findByNameAndDescription(paramObject.getText(), paramObject.getPageable()))
                .thenReturn(List.of(item));
        when(itemRepo.findAllByOwnerId(USER_ID, paramObject.getPageable())).thenReturn(List.of(item));
        when(itemRepo.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(itemRepo.save(item)).thenReturn(item);
        when(itemMapper.toResponse(item)).thenReturn(itemOutOwnerDto);
        when(itemMapper.toOwnerResponse(item)).thenReturn(itemOutOwnerDto);
        doAnswer(invocationOnMock -> {
            Item i = invocationOnMock.getArgument(0, Item.class);
            ItemInputDto p = invocationOnMock.getArgument(1, ItemInputDto.class);
            i.setDescription(p.getDescription());
            return true;
        }).when(patcher).patch(item, patch);
    }

    private void setUpUserLayer() {
        user = new User();
        user.setId(USER_ID);
        outsideUser = new User();
        outsideUser.setId(OUTSIDE_USER_ID);
        when(userRepo.existsById(USER_ID)).thenReturn(true);
        when(userRepo.existsById(OUTSIDE_USER_ID)).thenReturn(true);
    }

    private void setUpCommentLayer() {
        inDto = new CommentInputDto();
        inDto.setText(COMMENT_TEXT);
        comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText(COMMENT_TEXT);
        outDto = new CommentOutputDto();
        outDto.setId(COMMENT_ID);
        outDto.setText(COMMENT_TEXT);
        when(comMapper.toModel(inDto, ITEM_ID, USER_ID)).thenReturn(comment);
        when(comRepo.save(comment)).thenReturn(comment);
        when(comMapper.toResponse(comment)).thenReturn(outDto);
        when(bookRepo.findAllCompletedByBookerAndItem(any(), any())).thenReturn(Collections.emptyList());
        when(bookRepo.findAllCompletedByBookerAndItem(USER_ID, ITEM_ID)).thenReturn(List.of(new Booking()));
    }

    @Test
    void test1_bookerAddCommentToBookedItem() {
        CommentOutputDto dtoFromMethod = service.createComment(inDto, ITEM_ID, USER_ID);
        assertNotNull(dtoFromMethod);
        assertEquals(COMMENT_ID, dtoFromMethod.getId());
        assertNotNull(dtoFromMethod.getText());
        assertEquals(COMMENT_TEXT, dtoFromMethod.getText());

        verify(comMapper).toModel(inDto, ITEM_ID, USER_ID);
        verify(comMapper).toResponse(comment);
        verify(comRepo).save(comment);
        verify(bookRepo).findAllCompletedByBookerAndItem(USER_ID, ITEM_ID);
        verifyNoMoreInteractions(comMapper);
        verifyNoMoreInteractions(comRepo);
        verifyNoMoreInteractions(bookRepo);
    }

    @Test
    void test2_UserAddCommentToUnBookedItem() {
        Throwable throwable = assertThrows(
                NotAvailableException.class,
                () -> service.createComment(inDto, ITEM_ID, 999L)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test3_getALlByUser() {
        List<ItemOutputDto> dtos = service.getAllByUser(paramObject);
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        ItemOutputDto dto = dtos.get(0);
        assertNotNull(dto);
        assertEquals(ITEM_ID, dto.getId());
    }

    @Test
    void test4_getALlThrowIfUserNotFound() {
        paramObject = ItemsParamObject.newBuilder().withUserId(999L).build();
        assertThrows(NotFoundException.class, () -> service.getAllByUser(paramObject));
    }

    @Test
    void test5_ownerGetItemById() {
        ItemForOwnerOutputDto dto = (ItemForOwnerOutputDto) service.getItem(ITEM_ID, USER_ID);
        assertNotNull(dto);
        assertNotNull(dto.getLastBooking());
        assertNotNull(dto.getNextBooking());
        assertEquals(ITEM_ID, dto.getId());
    }

    @Test
    void test6_userGetItemById() {
        ItemForOwnerOutputDto dto = (ItemForOwnerOutputDto) service.getItem(ITEM_ID, OUTSIDE_USER_ID);
        assertNotNull(dto);
        assertNull(dto.getLastBooking());
        assertNull(dto.getNextBooking());
        assertEquals(ITEM_ID, dto.getId());
    }

    @Test
    void test7_userIsntExistGetItemById() {
        assertThrows(NotFoundException.class, () -> service.getItem(ITEM_ID, 999L));
    }

    @Test
    void test8_userGetItemIsntExist() {
        assertThrows(NotFoundException.class, () -> service.getItem(999L, USER_ID));
    }

    @Test
    void test9_ownerPatchItem() {
        ItemOutputDto dto = service.patchItem(USER_ID, ITEM_ID, patch);
        assertNotNull(dto);
        assertEquals(ITEM_ID, dto.getId());
        assertEquals(patch.getDescription(), dto.getDescription());

        when(patcher.patch(any(), any())).thenReturn(false);
        assertThrows(PatchException.class, () -> service.patchItem(USER_ID, ITEM_ID, patch));
    }

    @Test
    void test10_outsideUserAttemptPatchItem() {
        assertThrows(ForbiddenOperationException.class, () -> service.patchItem(OUTSIDE_USER_ID, ITEM_ID, patch));
    }

    @Test
    void test11_searchAndFoundItem() {
        List<ItemOutputDto> dtos = service.searchItem(paramObject);
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        ItemOutputDto dto = dtos.get(0);
        assertEquals(ITEM_ID, dto.getId());
    }

    @Test
    void test12_userIsntExistSearchItem() {
        paramObject = ItemsParamObject.newBuilder().withUserId(999L).build();
        assertThrows(NotFoundException.class, () -> service.searchItem(paramObject));
    }

    @Test
    void test13_searchByBlankText() {
        paramObject = ItemsParamObject.newBuilder().withUserId(USER_ID).withText(" ").build();
        List<ItemOutputDto> dtos = service.searchItem(paramObject);
        assertNotNull(dtos);
        assertEquals(0, dtos.size());
    }
}