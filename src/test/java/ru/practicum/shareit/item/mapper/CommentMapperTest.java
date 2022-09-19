package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.controller.dto.CommentInputDto;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.domain.Comment;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentMapperTest {
    private static final Long OWNER_ID = 1L;
    private static final Long AUTHOR_ID = 2L;
    private static final String AUTHOR_NAME = "Name";
    private static final Long ITEM_ID = 3L;
    private static final Long COMMENT_ID = 4L;
    private static final String COMMENT_TEXT = "Text for comment";

    @Mock
    private ItemReferenceMapper itemRefMap;
    @Mock
    private UserReferenceMapper userRefMap;
    @InjectMocks
    private CommentMapperImpl mapper;

    private User owner;
    private User author;
    private Item item;
    private CommentInputDto inDto;
    private Comment model;
    private CommentOutputDto outDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(OWNER_ID);

        author = new User();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);

        item = new Item();
        item.setId(ITEM_ID);
        item.setOwner(owner);

        inDto = new CommentInputDto();
        inDto.setText(COMMENT_TEXT);

        model = new Comment();
        model.setId(COMMENT_ID);
        model.setItem(item);
        model.setText(COMMENT_TEXT);
        model.setAuthor(author);

        outDto = new CommentOutputDto();
        outDto.setId(COMMENT_ID);
        outDto.setText(COMMENT_TEXT);
        outDto.setAuthorName(AUTHOR_NAME);
    }

    @Test
    void test1_toModel() {
        when(itemRefMap.idToItem(ITEM_ID)).thenReturn(item);
        when(userRefMap.idToUser(AUTHOR_ID)).thenReturn(author);

        Comment fromMapper = mapper.toModel(inDto, ITEM_ID, AUTHOR_ID);

        assertNotNull(fromMapper);
        assertEquals(COMMENT_TEXT, fromMapper.getText());
        assertEquals(AUTHOR_ID, fromMapper.getAuthor().getId());
        assertEquals(AUTHOR_NAME, fromMapper.getAuthor().getName());
        assertEquals(ITEM_ID, fromMapper.getItem().getId());
    }

    @Test
    void test2_toResponse() {
        CommentOutputDto fromMapper = mapper.toResponse(model);

        assertNotNull(fromMapper);
        assertEquals(COMMENT_ID, fromMapper.getId());
        assertEquals(COMMENT_TEXT, fromMapper.getText());
        assertEquals(AUTHOR_NAME, fromMapper.getAuthorName());
    }
}