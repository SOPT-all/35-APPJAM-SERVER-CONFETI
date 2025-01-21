package org.sopt.confeti.global.common;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorPage<T> {

    private final List<T> itemsWithNextCursor;
    private final int size;

    public static <T> CursorPage<T> of(final List<T> itemsWithNextCursor, final int size) {
        return new CursorPage<>(itemsWithNextCursor, size);
    }

    public boolean isLast() {
        return itemsWithNextCursor.size() <= size;
    }

    public List<T> getItems() {
        if (isLast()) {
            return itemsWithNextCursor;
        }

        return itemsWithNextCursor.subList(0, size);
    }

    public T getNextCursor() {
        return itemsWithNextCursor.get(size - 1);
    }
}
