package ru.practicum.shareit.requests.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.domain.Request;
import ru.practicum.shareit.user.domain.User;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class JpaRequestRepositoryTest {
    @Autowired
    TestEntityManager em;
    @Autowired
    RequestRepository repo;

    @Test
    void findAllRequestsButUser() {
        User requester = getUser();
        User outsideUser = getUser();
        Request targetRequest = getRequest(requester);
        Request targetRequest1 = getRequest(requester);
        Request outsideRequest = getRequest(outsideUser);
        em.persist(requester);
        em.persist(outsideUser);
        em.persist(targetRequest);
        em.persist(targetRequest1);
        em.persist(outsideRequest);

        List<Request> requests = repo.findAllRequestsButUser(outsideUser.getId(), 0, 10);
        assertThat(requests).hasSize(2).contains(targetRequest, targetRequest1);
    }

    private Request getRequest(User requester) {
        Request request = new Request();
        request.setRequester(requester);
        request.setDescription("descr");
        return request;
    }

    private User getUser() {
        User user = new User();
        user.setName("user-puser");
        user.setEmail(generateRandomEmail(7));
        return user;
    }

    private String generateRandomEmail(int len) {
        Random rnd = new Random();
        StringBuilder mailBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = rnd.nextInt(25);
            char c = (char) (97 + index);
            mailBuilder.append(c);
        }
        mailBuilder.append("@mail.com");
        return mailBuilder.toString();
    }
}