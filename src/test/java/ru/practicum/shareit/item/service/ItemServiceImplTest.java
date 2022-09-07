package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.controller.dto.ItemRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.domain.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final ItemMapper mapper;
    private final EntityManager em;

    @Test
    void test1_shouldCreateItem() {
        UserDto user = new UserDto();
        user.setName("Test user");
        user.setEmail("test@test.ru");
        userService.createUser(user);
        ItemRequest dto = new ItemRequest();
        dto.setAvailable(true);
        dto.setDescription("Test description for test item");
        dto.setName("Test item");
        ItemResponse response = itemService.createItem(1, dto);

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class);
        Item fromDb = query.setParameter("id", response.getId()).getSingleResult();
        ItemResponse responseFromDb = mapper.toResponse(fromDb);

        assertThat(responseFromDb.getId(), equalTo(response.getId()));
    }
}
