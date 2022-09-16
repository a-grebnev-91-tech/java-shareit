package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.requests.repository.OffsetPageable;

@Getter
@Setter
@AllArgsConstructor
public class ItemsParamObject {
    public static final String ITEMS_DEFAULT_SORT_BY = "id";
    public static final String ITEMS_DEFAULT_ORDER = "ASC";
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_SIZE = 20;

    private final Long userId;
    private final String text;
    private final Pageable pageable;

    private ItemsParamObject(ItemsParamBuilder builder) {
        this.userId = builder.userId;
        this.text = builder.text;
        String sortBy  = builder.sortBy == null ? ITEMS_DEFAULT_SORT_BY : builder.sortBy;
        String sortOrder = builder.sortOrder == null ? ITEMS_DEFAULT_ORDER : builder.sortOrder;
        int offset = builder.from == null ? DEFAULT_OFFSET : builder.from;
        int size = builder.size == null ? DEFAULT_SIZE : builder.size;
        Sort sort = Sort.by(Sort.Direction.valueOf(sortOrder), sortBy);
        this.pageable = OffsetPageable.of(offset, size, sort);
    }

    public static ItemsParamBuilder newBuilder() {
        return new ItemsParamBuilder();
    }

    public static final class ItemsParamBuilder {
        private Long userId;
        private String text;
        private String sortBy;
        private String sortOrder;
        private Integer from;
        private Integer size;

        private ItemsParamBuilder() {
        }

        public ItemsParamBuilder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ItemsParamBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public ItemsParamBuilder from(Integer from) {
            this.from = from;
            return this;
        }

        public ItemsParamBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public ItemsParamBuilder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public ItemsParamBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public ItemsParamObject build() {
            return new ItemsParamObject(this);
        }
    }
}
