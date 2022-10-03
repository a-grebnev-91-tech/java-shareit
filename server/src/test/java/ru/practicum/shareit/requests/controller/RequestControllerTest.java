package ru.practicum.shareit.requests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;
import ru.practicum.shareit.requests.domain.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {
    @Autowired
    ObjectMapper jsonMapper;
    @MockBean
    RequestService service;
    @Autowired
    MockMvc mockMvc;
    RequestInputDto inputDto;
    RequestOutputDto outputDto;
    ItemOutputDto itemOutputDto;

    private static final HttpHeaders HEADER_WITH_USER_ID = new HttpHeaders();
    private static final String REQUEST_DESCRIPTION = "Test item request";
    private static final Long USER_ID = 1L;
    private static final String ROOT_PATH = "/requests";
    private static final Long ITEM_ID = 5L;
    private static final String ITEM_NAME = "test item";
    private static final String ITEM_DESCRIPTION = "description for test item";
    private static final boolean ITEM_AVAILABLE = true;

    @BeforeAll
    private void initInput() {
        inputDto = new RequestInputDto();
        inputDto.setDescription(REQUEST_DESCRIPTION);

        outputDto = new RequestOutputDto();
        outputDto.setId(1L);
        outputDto.setDescription(REQUEST_DESCRIPTION);
        outputDto.setItems(List.of(getItemOutputDto()));

        HEADER_WITH_USER_ID.add(USER_ID_HEADER, USER_ID.toString());
    }

    private ItemOutputDto getItemOutputDto() {
        itemOutputDto = new ItemOutputDto();
        itemOutputDto.setId(ITEM_ID);
        itemOutputDto.setName(ITEM_NAME);
        itemOutputDto.setDescription(ITEM_DESCRIPTION);
        itemOutputDto.setAvailable(ITEM_AVAILABLE);
        return itemOutputDto;
    }

    @Test
    void test1_shouldCreateValidRequest() throws Exception {
        when(service.createRequest(USER_ID, inputDto)).thenReturn(outputDto);

        mockMvc.perform(post(ROOT_PATH)
                        .content(jsonMapper.writeValueAsString(inputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(HEADER_WITH_USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outputDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(outputDto.getDescription())))
                .andExpect(jsonPath("$.created", is(outputDto.getCreated()), LocalDateTime.class))
                .andExpect(jsonPath("$.items.length()", is(1)))
                .andExpect(jsonPath("$.items[0]", is(itemOutputDto), ItemOutputDto.class));
    }

    @Test
    void test2_creatingRequestShouldReturnNotFoundForInvalidUserId() throws Exception {
        Long invalidUserId = -1L;
        HttpHeaders invalidUserHeader = new HttpHeaders();
        invalidUserHeader.add(USER_ID_HEADER, invalidUserId.toString());

        when(service.createRequest(invalidUserId, inputDto)).thenThrow(NotFoundException.class);

        mockMvc.perform(post(ROOT_PATH)
                        .content(jsonMapper.writeValueAsString(inputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(invalidUserHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void test4_gettingRequestsByValidUserShouldReturnOk() throws Exception {
        when(service.getAllRequestsByUser(USER_ID)).thenReturn(List.of(outputDto));

        mockMvc.perform(get(ROOT_PATH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .headers(HEADER_WITH_USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(outputDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(outputDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(outputDto.getCreated()), LocalDateTime.class))
                .andExpect(jsonPath("$[0].items.length()", is(1)))
                .andExpect(jsonPath("$[0].items[0]", is(itemOutputDto), ItemOutputDto.class));
    }

    @Test
    void test5_gettingRequestsByUserShouldReturnNotFoundForInvalidUserId() throws Exception {
        when(service.getAllRequestsByUser(USER_ID)).thenThrow(NotFoundException.class);

        mockMvc.perform(get(ROOT_PATH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .headers(HEADER_WITH_USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void test6_gettingRequestsByUserShouldReturnEmptyForUserWithoutRequests() throws Exception {
        when(service.getAllRequestsByUser(USER_ID)).thenReturn(List.of());

        mockMvc.perform(get(ROOT_PATH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .headers(HEADER_WITH_USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void test7_getAllRequestsButUser() throws Exception {
        when(service.getAllRequestsButUser(USER_ID, 0, 10)).thenReturn(List.of(outputDto));

        mockMvc.perform(get(ROOT_PATH + "/all?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .headers(HEADER_WITH_USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void test8_getRequestById() throws Exception {
        when(service.getRequestById(anyLong(), anyLong())).thenReturn(outputDto);

        mockMvc.perform(get(ROOT_PATH + "/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .headers(HEADER_WITH_USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}