package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemsParamObject;
import ru.practicum.shareit.item.domain.ItemService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerParameterObjectTest {
    @Mock
    ItemService service;
    @InjectMocks
    ItemController controller;
    private static final int from = 1;
    private static final int size = 10;
    private static final Long userId = 1L;
    private static final String sortBy = "id";
    private static final  String orderBy = "ASC";
    private static final String text = "text";

    @Test
    void test1_getAllByOwnerShouldConvertArgsToParamObj() {
        controller.getAll(userId, from, size, sortBy, orderBy);

        ArgumentCaptor<ItemsParamObject> captor = ArgumentCaptor.forClass(ItemsParamObject.class);
        verify(service).getAllByUser(captor.capture());
        verifyNoMoreInteractions(service);
        ItemsParamObject capturedParam = captor.getValue();

        assertNotNull(capturedParam);
        assertEquals(userId, capturedParam.getUserId());
        assertNotNull(capturedParam.getPageable());
        assertEquals(from, capturedParam.getPageable().getOffset());
        assertEquals(size, capturedParam.getPageable().getPageSize());
        assertTrue(capturedParam.getPageable().getSort().isSorted());
        String[] sortParam = capturedParam.getPageable().getSort().toString().split(":");
        assertEquals(sortBy, sortParam[0].trim());
        assertEquals(orderBy, sortParam[1].trim());
        assertNull(capturedParam.getText());
    }

    @Test
    void test2_searchShouldConvertArgsToParamObj() {
        controller.searchItems(text, userId, from, size, sortBy, orderBy);

        ArgumentCaptor<ItemsParamObject> captor = ArgumentCaptor.forClass(ItemsParamObject.class);
        verify(service).searchItem(captor.capture());
        verifyNoMoreInteractions(service);
        ItemsParamObject capturedParam = captor.getValue();

        assertNotNull(capturedParam);
        assertEquals(userId, capturedParam.getUserId());
        assertNotNull(capturedParam.getText());
        assertEquals(text, capturedParam.getText());
        assertNotNull(capturedParam.getPageable());
        assertEquals(from, capturedParam.getPageable().getOffset());
        assertEquals(size, capturedParam.getPageable().getPageSize());
        assertTrue(capturedParam.getPageable().getSort().isSorted());
        String[] sortParam = capturedParam.getPageable().getSort().toString().split(":");
        assertEquals(sortBy, sortParam[0].trim());
        assertEquals(orderBy, sortParam[1].trim());
    }
}