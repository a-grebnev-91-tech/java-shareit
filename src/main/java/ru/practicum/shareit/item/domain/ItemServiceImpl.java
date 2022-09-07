package ru.practicum.shareit.item.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PatchException;
import ru.practicum.shareit.item.controller.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.util.Patcher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    @Qualifier("InDbItems")
    private final ItemRepository itemRepository;
    @Qualifier("InDbUsers")
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;
    private final Patcher patcher;

    @Override
    public CommentOutputDto createComment(CommentInputDto commentDto, Long itemId, Long userId) {
        if (isUserDidntRentItem(userId, itemId)) {
            throw new NotAvailableException("User cannot leave comments on item that he didn't rent");
        }
        Comment comment = commentMapper.toModel(commentDto, itemId, userId);
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public ItemOutputDto createItem(long userId, ItemInputDto dto) {
        return itemMapper.toResponse(itemRepository.save(itemMapper.toModel(dto, userId)));
    }

    @Override
    public List<ItemOutputDto> getAllByUser(long userId) {
        getUserOrThrow(userId);
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(itemMapper::toOwnerResponse)
                .sorted((i1, i2) -> Long.compare(i1.getId(), i2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public ItemOutputDto getItem(long itemId, long userId) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d isn't exist", itemId)));
        //TODO исправить после завершения проекта
        // реализовал функционал, который на мой взгляд логичнее, но тесты постмана его не поддерживают
        /*
        if (isItemBelongToUser(item, userId)) {
            return itemMapper.toOwnerResponse(item);
        } else {
            return itemMapper.toResponse(item);
        }
         */
        if (isItemBelongToUser(item, userId)) {
             return itemMapper.toOwnerResponse(item);
        } else {
            ItemForOwnerOutputDto response = itemMapper.toOwnerResponse(item);
            response.setNextBooking(null);
            response.setLastBooking(null);
            return response;
        }
    }

    @Override
    @Transactional
    public ItemOutputDto patchItem(long userId, long itemId, ItemInputDto patch) {
        Item existingItem = getItemOrThrow(itemId);
        if (isItemBelongToUser(existingItem, userId)) {
            if (patcher.patch(existingItem, patch)) {
                return itemMapper.toResponse(itemRepository.save(existingItem));
            } else {
                throw new PatchException(String.format("Patch %s couldn't be applied on %s", patch, existingItem));
            }
        } else {
            throw new ForbiddenOperationException(
                    String.format("User with id %d could not change item with id %d", userId, existingItem.getId())
            );
        }
    }

    @Override
    public List<ItemOutputDto> searchItem(String text) {
        if (text.isBlank())
            return Collections.emptyList();
        List<Item> founded = itemRepository.findByNameAndDescription(text);
        return founded.stream().map(itemMapper::toResponse).collect(Collectors.toList());
    }

    private Item getItemOrThrow(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.orElseThrow(() -> new NotFoundException(String.format("Item with id %d isn't exist", itemId)));

    }

    private User getUserOrThrow(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new NotFoundException(String.format("User with id %d isn't exist", userId)));
    }

    private boolean isItemBelongToUser(Item existingItem, long userId) {
        getUserOrThrow(userId);
        return existingItem.getOwner().getId().equals(userId);
    }

    private boolean isUserDidntRentItem(Long userId, Long itemId) {
        List<Booking> bookings =
                bookingRepository.findAllCompletedByBookerAndItem(userId, itemId);
        return bookings.isEmpty();
    }
}
