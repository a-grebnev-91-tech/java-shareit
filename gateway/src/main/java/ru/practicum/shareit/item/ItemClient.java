package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> createComment(CommentInputDto comment, long itemId, long userId) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    public ResponseEntity<Object> createItem(long userId, ItemInputDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> getAllByUser(long userId, int from, int size, String sortBy, String order) {
        Map<String, Object> params = Map.of(
                "from", from,
                "size", size,
                "sortBy", sortBy,
                "order", order
        );
        return get("?from={from}&size={size}&sortBy={sortBy}&order={order}", userId, params);
    }

    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> patchItem(long userId, long itemId, ItemInputDto patchRequest) {
        return patch("/" + itemId, userId, patchRequest);
    }

    public ResponseEntity<Object> searchItem(
            String text,
            long userId,
            int from,
            int size,
            String sortBy,
            String order
    ) {
        Map<String, Object> params = Map.of(
                "text", text,
                "from", from,
                "size", size,
                "sortBy", sortBy,
                "order", order
        );
        return get("/search?text={text}&from={from}&size={size}&sortBy={sortBy}&order={order}", userId, params);
    }
}