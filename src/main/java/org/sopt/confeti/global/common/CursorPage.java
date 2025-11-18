package org.sopt.confeti.global.common;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Common DTO for Cursor-Based Paging Implementation
 * @author chyun
 * @param <T> T is the target to be retrieved using paging.
 * itemsWithNextCursor is the retrieved list.
 *
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorPage<T> {

    /**
     * itemsWithNextCursor is the retrieved list.
     */
    private final List<T> itemsWithNextCursor;

    /**
     * size is the number of items you attempted to retrieve.
     * You must add one to the number of items required.
     * For example, if you intend to respond with 10 items, you should retrieve 11 items, and the size value should be 11.
     */
    private final int size;

    public static <T> CursorPage<T> of(final List<T> itemsWithNextCursor, final int size) {
        return new CursorPage<>(itemsWithNextCursor, size);
    }

    /**
     * Returns whether it is the last page
     * @return boolean
     */
    public boolean isLast() {
        return itemsWithNextCursor.size() < size;
    }

    /**
     * Returns the list of retrieved items.
     * @return List of items of type T
     */
    public List<T> getItems() {
        if (isLast()) {
            return itemsWithNextCursor;
        }

        return itemsWithNextCursor.subList(0, size - 1);
    }

    /**
     * Returns the item pointed to by the next cursor.
     * @return T
     */
    public T getNextCursor() {
        if (isLast()) return null;

        return itemsWithNextCursor.getLast();
    }
}
