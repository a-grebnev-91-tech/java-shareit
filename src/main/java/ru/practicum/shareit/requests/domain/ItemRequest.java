package ru.practicum.shareit.requests.domain;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "created")
    private LocalDateTime created;
    @OneToMany
    List<Item> responses;
}
