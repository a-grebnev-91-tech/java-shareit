package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.domain.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentReferenceMapperTest {
    private static final Long ITEM_ID = 3L;
    @Mock
    private CommentRepository comRepo;
    @Mock
    private CommentMapper mapper;
    @InjectMocks
    private CommentReferenceMapper refMap;

    @Test
    void test1_shouldMapItemToCommentsList() {
        Comment com1 = new Comment();
        com1.setId(1L);
        Comment com2 = new Comment();
        com2.setId(2L);

        List<Comment> comments = new ArrayList<>();
        comments.add(com1);
        comments.add(com2);

        CommentOutputDto dto1 = new CommentOutputDto();
        dto1.setId(com1.getId());
        CommentOutputDto dto2 = new CommentOutputDto();
        dto2.setId(com2.getId());

        when(comRepo.findAllByItemId(ITEM_ID)).thenReturn(comments);
        when(mapper.toResponse(com1)).thenReturn(dto1);
        when(mapper.toResponse(com2)).thenReturn(dto2);

        List<CommentOutputDto> output = refMap.map(ITEM_ID);
        assertNotNull(output);
        assertEquals(2, output.size());
    }
}