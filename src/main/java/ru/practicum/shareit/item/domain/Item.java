package ru.practicum.shareit.item.domain;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.domain.User;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column(name = "available")
    private boolean available;
}
