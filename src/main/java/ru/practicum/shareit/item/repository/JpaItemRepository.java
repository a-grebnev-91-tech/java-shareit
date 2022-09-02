package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.domain.Item;

import java.util.List;

@Repository("InDbItems")
public interface JpaItemRepository extends JpaRepository<Item, Long>, ItemRepository {
    @Override
    @Query(value = "SELECT * FROM items " +
            "WHERE (name ILIKE '%' || ?1 || '%' OR " +
            "description ILIKE '%' || ?1 || '%') " +
            "AND available",
            nativeQuery = true)
    List<Item> findByNameAndDescription(String text);
}
