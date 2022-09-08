package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.domain.Request;

import java.util.List;

@Repository
public interface JpaRequestRepository extends JpaRepository<Request, Long>, RequestRepository {
    @Override
    default List<Request> findAllRequests(Integer from, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");

    }


}
