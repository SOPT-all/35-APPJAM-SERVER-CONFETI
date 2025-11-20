package org.sopt.confeti.domain.timetable_festival;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimetableFestivalCursor {

    private static final String DELIMITER = "@";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * createdAt과 id를 Base64로 인코딩된 cursor로 변환
     */
    public static String encode(LocalDate startAt, long timetableFestivalId) {
        if (startAt == null || timetableFestivalId <= 0) {
            return null;
        }

        String raw = startAt.format(FORMATTER) + DELIMITER + timetableFestivalId;
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64로 인코딩된 cursor를 디코딩하여 CursorData로 변환
     */
    public static CursorData decode(String encodedCursor) {
        if (encodedCursor == null || encodedCursor.isBlank()) {
            return null;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedCursor);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            String[] parts = decodedString.split(DELIMITER);
            if (parts.length != 2) {
                log.error("TimetableFestivalCursor.decode: The number of decoded parts must be two. Parts : Encoded cursor : {}", encodedCursor);
                throw new ConfetiException(ErrorMessage.BAD_REQUEST);
            }

            LocalDate startAt = LocalDate.parse(parts[0], FORMATTER);
            long timetableFestivalId = Long.parseLong(parts[1]);

            return new CursorData(startAt, timetableFestivalId);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            log.error("TimetableFestivalCursor.decode: An error occurred during the decoding process. Encoded cursor : {}", encodedCursor);
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    /**
     * 디코딩된 커서 데이터
     */
    public record CursorData(
            LocalDate startAt,
            long timetableFestivalId
    ) {
    }
}
