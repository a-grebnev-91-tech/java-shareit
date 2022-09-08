package ru.practicum.shareit.requests.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;
import ru.practicum.shareit.requests.domain.Request;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {
    @Mock
    ItemMapper itemMapper;
    @Mock
    UserReferenceMapper userRefMapper;
    @InjectMocks
    RequestMapper mapper = Mappers.getMapper(RequestMapper.class);
    private final long testUserId = 1;
    private final long testRequestId = 3;

    @Test
    void test1_shouldMapModelToDto() {
        User user = getTestUser();
        Item item = getTestItem(user);
        ItemOutputDto itemForDto = getItemOutputDto(item);
        when(itemMapper.toResponse(item)).thenReturn(itemForDto);

        Request testModel = getTestRequest(item, user);
        RequestOutputDto testDto = mapper.modelToDto(testModel);
        List<ItemOutputDto> dtoItems = testDto.getItems();
        List<Item> modelItems = testModel.getResponses();

        assertEquals(testRequestId, testDto.getId());
        assertEquals(testModel.getDescription(), testDto.getDescription());

        assertNotNull(dtoItems);
        assertThat(dtoItems.size(), equalTo(modelItems.size()));

        ItemOutputDto itemFromDto = dtoItems.get(0);

        assertEquals(itemForDto, itemFromDto);
    }

    @Test
    void test2_shouldMapDtoToModel() {
        User user = getTestUser();
        when(userRefMapper.map(testUserId)).thenReturn(user);

        RequestInputDto testDto = getItemInputDto();
        Request testModel = mapper.dtoToModel(testDto, testUserId);

        assertEquals(testDto.getDescription(), testModel.getDescription());
        assertEquals(user, testModel.getRequester());
    }

    private RequestInputDto getItemInputDto() {
        RequestInputDto dto = new RequestInputDto();
        dto.setDescription("Test description");
        return dto;
    }

    private ItemOutputDto getItemOutputDto(Item item) {
        ItemOutputDto dto = new ItemOutputDto();
        dto.setId(item.getId());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setName(item.getName());
        return dto;
    }

    private Request getTestRequest(Item item, User user) {
        Request testRequest = new Request();
        testRequest.setId(testRequestId);
        testRequest.setRequester(user);
        testRequest.setDescription("I need more tests");
        testRequest.setResponses(Collections.singletonList(item));
        return testRequest;
    }

    private Item getTestItem(User owner) {
        Item item = new Item();
        long testItemId = 2;
        item.setId(testItemId);
        item.setName("Test item");
        item.setDescription("Simple test item");
        item.setAvailable(true);
        item.setOwner(owner);
        return item;
    }

    private User getTestUser() {
        User testUser = new User();
        testUser.setId(testUserId);
        testUser.setEmail("test@test.te");
        testUser.setName("Test user");
        return testUser;
    }

}