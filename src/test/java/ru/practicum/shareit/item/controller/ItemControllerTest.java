package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingRequestHeaderException;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.controller.dto.CommentInputDto;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemInputDto;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.item.domain.ItemService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 2L;
    private static final String ITEM_NAME = "Item";
    private static final String ITEM_DESCRIPTION = "Description for item";
    private static final Boolean ITEM_AVAILABLE = true;
    private static final Long COMMENT_ID = 3L;
    private static final String COMMENT_TEXT = "text for comment";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemService service;
    @Autowired
    private ObjectMapper mapper;

    private ItemInputDto inputDto;
    private ItemOutputDto outputDto;
    private CommentInputDto commentInDto;
    private CommentOutputDto commentOutDto;

    @BeforeEach
    void setUp() {
        inputDto = new ItemInputDto();
        inputDto.setName(ITEM_NAME);
        inputDto.setDescription(ITEM_DESCRIPTION);
        inputDto.setAvailable(ITEM_AVAILABLE);

        outputDto = new ItemOutputDto();
        outputDto.setId(ITEM_ID);
        outputDto.setName(ITEM_NAME);
        outputDto.setDescription(ITEM_DESCRIPTION);
        outputDto.setAvailable(ITEM_AVAILABLE);

        commentInDto = new CommentInputDto();
        commentInDto.setText(COMMENT_TEXT);

        commentOutDto = new CommentOutputDto();
        commentOutDto.setId(COMMENT_ID);
        commentOutDto.setText(COMMENT_TEXT);
    }

    @Test
    void test1_createComment() throws Exception {
        when(service.createComment(any(), anyLong(), anyLong()))
                .thenReturn(commentOutDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentInDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentOutDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentOutDto.getText()), String.class));
    }

    @Test
    void test2_createItem() throws Exception {
        when(service.createItem(anyLong(), any()))
                .thenReturn(outputDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outputDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(outputDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(outputDto.getDescription()), String.class));
    }

    @Test
    void test3_getItem() throws Exception {
        when(service.getItem(anyLong(), anyLong()))
                .thenReturn(outputDto);

        mvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outputDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(outputDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(outputDto.getDescription()), String.class));
    }

    @Test
    void test4_patchItem() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenReturn(outputDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outputDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(outputDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(outputDto.getDescription()), String.class));
    }

    @Test
    void test6_throwEx() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenThrow(new ConstraintViolationException("message", null));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test7_throwEx() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenThrow(new ConflictEmailException("message"));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void test8_throwEx() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenThrow(new ForbiddenOperationException("message"));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void test9_throwEx() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenThrow(new IllegalArgumentException("message"));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test10_throwEx() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenThrow(new NotAvailableException("message"));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test11_throwEx() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenThrow(new BookingStateIsNotSupportedException("message"));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test12_throwEx() throws Exception {
        when(service.patchItem(anyLong(), anyLong(), any()))
                .thenThrow(new PatchException("message"));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}