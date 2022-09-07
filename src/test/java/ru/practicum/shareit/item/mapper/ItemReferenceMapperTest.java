package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemReferenceMapperTest {
    @Mock
    ItemRepository repo;

    @Test
    void test1_shouldReturnUserByIdOrThrow() {
        long validId = 1;
        long invalidId = -1;
        Item item = new Item();
        item.setId(validId);

        Mockito.when(repo.findById(validId)).thenReturn(Optional.of(item));
        ItemReferenceMapper mapper = new ItemReferenceMapper(repo);

        assertEquals(item, mapper.map(validId));
        NotFoundException ex = assertThrows(NotFoundException.class, () -> mapper.map(invalidId));
        assertEquals(String.format("Item with id %d isn't exist", invalidId), ex.getMessage());

        Mockito.verify(repo).findById(validId);
        Mockito.verify(repo).findById(invalidId);
        Mockito.verifyNoMoreInteractions(repo);
    }

    @Test
    void test2_shouldReturnIdByUser() {
        long id = 1;
        Item item = new Item();
        item.setId(id);

        ItemReferenceMapper mapper = new ItemReferenceMapper(repo);

        assertEquals(id, mapper.map(item));
    }
}