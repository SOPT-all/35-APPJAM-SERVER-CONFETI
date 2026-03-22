package org.sopt.confeti.api.admin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            validateDatesRequireArtists();
        }

        if (hasTimetableInfo()) {
            validateTimetableDuration();
            validateAllDatesHaveStages();
            validateArtistTimetableMapping();
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

    private void validateDatesRequireArtists() {
        boolean hasDateWithoutArtists = dates.stream()
            .anyMatch(date -> date.artistIds() == null || date.artistIds().isEmpty());

        if (hasDateWithoutArtists) {
            log.warn(
                "PutAdminFestivalRequest.validateDatesRequireArtists : 모든 날짜에 아티스트 목록이 필요합니다.");
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
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
