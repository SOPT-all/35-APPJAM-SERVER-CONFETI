package org.sopt.confeti.api.admin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.admin.facade.dto.request.AdminFestivalCommand;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;

@Slf4j
public record PutAdminFestivalRequest(
    Long festivalId,
    @NotBlank String title,
    @NotNull LocalDate startAt,
    @NotNull LocalDate endAt,
    @NotBlank String area,
    @NotBlank String ageRating,
    @NotBlank String time,
    @NotBlank String price,
    @NotBlank String address,
    @NotNull @Valid List<ReservationUrlRequest> reservationUrls,
    @NotEmpty List<@Valid ReservationScheduleRequest> reservationSchedules,
    List<@Valid DateRequest> dates
) {

    public AdminFestivalCommand toCommand() {
        boolean hasTimetable = hasTimetableInfo();
        validate(hasTimetable);

        TimetableSupportStatus status = hasTimetable
            ? TimetableSupportStatus.SUPPORTED
            : TimetableSupportStatus.NOT_SUPPORTED;

        return AdminFestivalCommand.of(
            festivalId, title, startAt, endAt, area,
            ageRating, time, price, address, status,
            reservationUrls.stream()
                .map(url -> AdminFestivalCommand.ReservationUrlCommand.of(
                    url.ticketVendorId(), url.reservationUrl()))
                .toList(),
            reservationSchedules.stream()
                .map(s -> AdminFestivalCommand.ReservationScheduleCommand.of(
                    s.roundName(), s.reserveAt()))
                .toList(),
            dates != null
                ? dates.stream().map(this::toDateCommand).toList()
                : List.of()
        );
    }

    private AdminFestivalCommand.DateCommand toDateCommand(DateRequest date) {
        return AdminFestivalCommand.DateCommand.of(
            date.festivalDateId(), date.festivalAt(), date.openAt(),
            date.artistIds() != null ? date.artistIds() : List.of(),
            date.stages() != null
                ? date.stages().stream().map(this::toStageCommand).toList()
                : List.of()
        );
    }

    private AdminFestivalCommand.StageCommand toStageCommand(StageRequest stage) {
        return AdminFestivalCommand.StageCommand.of(
            stage.festivalStageId(), stage.name(), stage.order(),
            stage.times().stream().map(this::toTimeCommand).toList()
        );
    }

    private AdminFestivalCommand.TimeCommand toTimeCommand(TimeRequest time) {
        return AdminFestivalCommand.TimeCommand.of(
            time.festivalTimeId(), time.startAt(), time.endAt(),
            time.name(), time.artistIds()
        );
    }

    private void validate(boolean hasTimetable) {
        validateDuration();
        validateReserveDate();

        if (dates != null && !dates.isEmpty()) {
            validateDateFestivalAtInRange();
        }

        if (hasTimetable) {
            validateAllDatesHaveStages();
            validateArtistTimetableMapping();
            validateOpenAtBeforeFirstTime();
            validateStageOrderUnique();
            validateTimeStartBeforeEnd();
            validateTimeNoOverlap();
        }
    }

    private void validateDuration() {
        if (startAt.isAfter(endAt)) {
            log.warn("PutAdminFestivalRequest.validateDuration : 시작 날짜는 끝 날짜보다 작거나 같아야 합니다.");
            throw new BadRequestException(ErrorMessage.FESTIVAL_INVALID_DURATION);
        }
    }

    private void validateReserveDate() {
        for (ReservationScheduleRequest schedule : reservationSchedules) {
            if (!schedule.reserveAt().isBefore(startAt.atStartOfDay())) {
                log.warn(
                    "PutAdminFestivalRequest.validateReserveDate : 예약 일자({})는 시작 날짜({})보다 작아야 합니다.",
                    schedule.reserveAt(), startAt);
                throw new BadRequestException(ErrorMessage.FESTIVAL_INVALID_RESERVE_DATE);
            }
        }
    }

    private void validateAllDatesHaveStages() {
        boolean hasDateWithoutStages = dates.stream()
            .anyMatch(date -> date.stages() == null || date.stages().isEmpty());

        if (hasDateWithoutStages) {
            log.warn(
                "PutAdminFestivalRequest.validateAllDatesHaveStages : 타임테이블이 존재하면 모든 날짜에 스테이지 정보가 있어야 합니다.");
            throw new BadRequestException(ErrorMessage.FESTIVAL_TIMETABLE_DATE_NO_STAGE);
        }
    }

    private void validateArtistTimetableMapping() {
        for (DateRequest date : dates) {
            Set<String> dateArtistIds = new HashSet<>(
                date.artistIds() != null ? date.artistIds() : List.of());
            Set<String> timetableArtistIds = date.stages().stream()
                .flatMap(stage -> stage.times().stream())
                .flatMap(time -> time.artistIds().stream())
                .collect(Collectors.toSet());

            if (!timetableArtistIds.equals(dateArtistIds)) {
                log.warn(
                    "PutAdminFestivalRequest.validateArtistTimetableMapping : 날짜({})의 아티스트와 타임테이블 아티스트가 일치하지 않습니다.",
                    date.festivalAt());
                throw new BadRequestException(ErrorMessage.FESTIVAL_TIMETABLE_ARTIST_NOT_MAPPED);
            }
        }
    }

    private void validateDateFestivalAtInRange() {
        boolean hasDateOutOfRange = dates.stream()
            .anyMatch(date -> date.festivalAt().isBefore(startAt) || date.festivalAt().isAfter(endAt));

        if (hasDateOutOfRange) {
            log.warn(
                "PutAdminFestivalRequest.validateDateFestivalAtInRange : 모든 날짜의 festivalAt은 공연 기간({} ~ {}) 내에 있어야 합니다.",
                startAt, endAt);
            throw new BadRequestException(ErrorMessage.FESTIVAL_DATE_OUT_OF_RANGE);
        }
    }

    private void validateOpenAtBeforeFirstTime() {
        for (DateRequest date : dates) {
            LocalTime earliestStartAt = date.stages().stream()
                .flatMap(stage -> stage.times().stream())
                .map(TimeRequest::startAt)
                .min(LocalTime::compareTo)
                .orElse(null);

            if (earliestStartAt != null && date.openAt().isAfter(earliestStartAt)) {
                log.warn(
                    "PutAdminFestivalRequest.validateOpenAtBeforeFirstTime : 날짜({})의 openAt({})은 가장 이른 공연 시작 시간({}) 이하여야 합니다.",
                    date.festivalAt(), date.openAt(), earliestStartAt);
                throw new BadRequestException(ErrorMessage.FESTIVAL_TIMETABLE_OPEN_AT_AFTER_FIRST_TIME);
            }
        }
    }

    private void validateStageOrderUnique() {
        for (DateRequest date : dates) {
            long distinctCount = date.stages().stream()
                .map(StageRequest::order)
                .distinct()
                .count();

            if (distinctCount != date.stages().size()) {
                log.warn(
                    "PutAdminFestivalRequest.validateStageOrderUnique : 날짜({})의 스테이지 순서(order)가 중복됩니다.",
                    date.festivalAt());
                throw new BadRequestException(ErrorMessage.FESTIVAL_TIMETABLE_STAGE_ORDER_DUPLICATE);
            }
        }
    }

    private void validateTimeStartBeforeEnd() {
        for (DateRequest date : dates) {
            for (StageRequest stage : date.stages()) {
                for (TimeRequest time : stage.times()) {
                    if (!time.startAt().isBefore(time.endAt())) {
                        log.warn(
                            "PutAdminFestivalRequest.validateTimeStartBeforeEnd : 날짜({}), 스테이지({})의 공연 시작 시간({})은 종료 시간({})보다 이전이어야 합니다.",
                            date.festivalAt(), stage.name(), time.startAt(), time.endAt());
                        throw new BadRequestException(ErrorMessage.FESTIVAL_TIMETABLE_TIME_START_AFTER_END);
                    }
                }
            }
        }
    }

    private void validateTimeNoOverlap() {
        for (DateRequest date : dates) {
            for (StageRequest stage : date.stages()) {
                List<TimeRequest> sortedTimes = new ArrayList<>(stage.times());
                sortedTimes.sort((a, b) -> a.startAt().compareTo(b.startAt()));

                for (int i = 0; i < sortedTimes.size() - 1; i++) {
                    TimeRequest current = sortedTimes.get(i);
                    TimeRequest next = sortedTimes.get(i + 1);

                    if (current.endAt().isAfter(next.startAt())) {
                        log.warn(
                            "PutAdminFestivalRequest.validateTimeNoOverlap : 날짜({}), 스테이지({})에서 공연 시간이 겹칩니다. ({} ~ {})와 ({} ~ {})",
                            date.festivalAt(), stage.name(),
                            current.startAt(), current.endAt(),
                            next.startAt(), next.endAt());
                        throw new BadRequestException(ErrorMessage.FESTIVAL_TIMETABLE_TIME_OVERLAP);
                    }
                }
            }
        }
    }

    private boolean hasTimetableInfo() {
        if (dates == null || dates.isEmpty()) {
            return false;
        }

        return dates.stream()
            .anyMatch(date -> date.stages() != null && !date.stages().isEmpty());
    }

    public record ReservationUrlRequest(
        @NotNull Long ticketVendorId,
        @NotBlank String reservationUrl
    ) {

    }

    public record ReservationScheduleRequest(
        @NotBlank @Size(max = 30) String roundName,
        @NotNull LocalDateTime reserveAt
    ) {

    }

    public record DateRequest(
        Long festivalDateId,
        @NotNull LocalDate festivalAt,
        @NotNull LocalTime openAt,
        List<@NotBlank String> artistIds,
        List<@Valid StageRequest> stages
    ) {

    }

    public record StageRequest(
        Long festivalStageId,
        @NotBlank String name,
        @NotNull Integer order,
        @NotEmpty List<@Valid TimeRequest> times
    ) {

    }

    public record TimeRequest(
        Long festivalTimeId,
        @NotNull LocalTime startAt,
        @NotNull LocalTime endAt,
        String name,
        @NotEmpty List<@NotBlank String> artistIds
    ) {

    }
}
