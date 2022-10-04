package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatcherTest {
    @Test
    void test1_patch() {
        Patcher patcher = new Patcher();
        User user = new User();
        user.setId(1L);
        User user1 = new User();
        Item item = new Item();

        assertTrue(patcher.patch(user, user1));
        assertFalse(patcher.patch(user, item));
    }
}