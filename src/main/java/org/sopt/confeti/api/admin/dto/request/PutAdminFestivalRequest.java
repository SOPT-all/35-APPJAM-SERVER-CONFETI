package org.sopt.confeti.api.admin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
import org.sopt.confeti.global.exception.ParameterInvalidException;
import org.sopt.confeti.global.message.ErrorMessage;

@Slf4j
public record PutAdminFestivalRequest(
    Long festivalId,
    @NotBlank String title,
    @NotNull LocalDate startAt,
    @NotNull LocalDate endAt,
    @NotBlank String area,
    @NotNull LocalDateTime reserveAt,
    @NotBlank String ageRating,
    @NotBlank String time,
    @NotBlank String price,
    @NotBlank String address,
    @NotNull @Valid List<ReservationUrlRequest> reservationUrls,
    List<@Valid DateRequest> dates
) {

    public AdminFestivalCommand toCommand() {
        validate();

        TimetableSupportStatus status = hasTimetableInfo()
            ? TimetableSupportStatus.SUPPORTED
            : TimetableSupportStatus.NOT_SUPPORTED;

        return AdminFestivalCommand.of(
            festivalId, title, startAt, endAt, area,
            reserveAt, ageRating, time, price, address, status,
            reservationUrls.stream()
                .map(url -> AdminFestivalCommand.ReservationUrlCommand.of(
                    url.ticketVendorId(), url.reservationUrl()))
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

    public void validate() {
        validateDuration();
        validateReserveDate();

        if (dates != null && !dates.isEmpty()) {
            validateDateFestivalAtInRange();
        }

        if (hasTimetableInfo()) {
            validateTimetableDuration();
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
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }
    }

    private void validateReserveDate() {
        if (!reserveAt.isBefore(startAt.atStartOfDay())) {
            log.warn("PutAdminFestivalRequest.validateReserveDate : 예약 일자는 시작 날짜보다 작아야 합니다.");
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }
    }

    private void validateTimetableDuration() {
        if (dates.isEmpty()) {
            log.warn(
                "PutAdminFestivalRequest.validateTimetableDuration : 타임테이블을 등록하려면 날짜 정보가 있어야 합니다.");
            throw new ParameterInvalidException(ErrorMessage.BAD_REQUEST);
        }

        LocalDate startDate = dates.stream().findFirst().get().festivalAt();
        LocalDate endDate = dates.stream().findFirst().get().festivalAt();

        for (DateRequest date : dates) {
            startDate = date.festivalAt.isBefore(startDate) ? date.festivalAt : startDate;
            endDate = date.festivalAt.isAfter(endDate) ? date.festivalAt : endDate;
        }

        if (startAt.isAfter(startDate)
            || endAt.isBefore(endDate)) {
            log.warn(
                "PutAdminFestivalRequest.validateTimetableDuration : 타임테이블의 날짜는 페스티벌의 공연 기간 안으로 지정해야합니다. 공연 기간 : {} ~ {}, 타임테이블 지정 날짜 : {} ~ {}",
                startAt, endAt, startDate, endDate);
            throw new ParameterInvalidException(ErrorMessage.BAD_REQUEST);
        }
    }

    private void validateAllDatesHaveStages() {
        boolean hasDateWithoutStages = dates.stream()
            .anyMatch(date -> date.stages() == null || date.stages().isEmpty());

        if (hasDateWithoutStages) {
            log.warn(
                "PutAdminFestivalRequest.validateAllDatesHaveStages : 타임테이블이 존재하면 모든 날짜에 스테이지 정보가 있어야 합니다.");
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }
    }

    private void validateArtistTimetableMapping() {
        for (DateRequest date : dates) {
            Set<String> dateArtistIds = new HashSet<>(date.artistIds());
            Set<String> timetableArtistIds = date.stages().stream()
                .flatMap(stage -> stage.times().stream())
                .flatMap(time -> time.artistIds().stream())
                .collect(Collectors.toSet());

            if (!timetableArtistIds.containsAll(dateArtistIds)) {
                log.warn(
                    "PutAdminFestivalRequest.validateArtistTimetableMapping : 날짜({})의 모든 아티스트가 타임테이블에 배정되어야 합니다.",
                    date.festivalAt());
                throw new BadRequestException(ErrorMessage.BAD_REQUEST);
            }

            if (!dateArtistIds.containsAll(timetableArtistIds)) {
                log.warn(
                    "PutAdminFestivalRequest.validateArtistTimetableMapping : 날짜({})의 타임테이블의 모든 아티스트가 선택되어야 합니다.",
                    date.festivalAt());
                throw new BadRequestException(ErrorMessage.BAD_REQUEST);
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
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
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
                throw new BadRequestException(ErrorMessage.BAD_REQUEST);
            }
        }
    }

    private void validateStageOrderUnique() {
        for (DateRequest date : dates) {
            List<Integer> orders = date.stages().stream()
                .map(StageRequest::order)
                .toList();
            Set<Integer> uniqueOrders = new HashSet<>(orders);

            if (uniqueOrders.size() != orders.size()) {
                log.warn(
                    "PutAdminFestivalRequest.validateStageOrderUnique : 날짜({})의 스테이지 순서(order)가 중복됩니다.",
                    date.festivalAt());
                throw new BadRequestException(ErrorMessage.BAD_REQUEST);
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
                        throw new BadRequestException(ErrorMessage.BAD_REQUEST);
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
                        throw new BadRequestException(ErrorMessage.BAD_REQUEST);
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
