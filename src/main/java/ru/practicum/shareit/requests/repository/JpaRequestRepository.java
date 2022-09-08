package ru.practicum.shareit.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.domain.Request;

@Repository
public interface JpaRequestRepository extends JpaRepository<Request, Long>, RequestRepository {
}
