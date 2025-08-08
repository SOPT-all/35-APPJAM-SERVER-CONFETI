package org.sopt.confeti.api.setlist.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchArtistMusicsResponse;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchMusicsResponse;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchPerformancesResponse;
import org.sopt.confeti.api.setlist.facade.SetlistSearchFacade;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchedPerformancesDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistMusicsDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchMusicsDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my/setlists/search")
public class SetlistSearchController {

    private final SetlistSearchFacade setlistSearchFacade;
    private final S3FileHandler s3FileHandler;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/performances")
    public ResponseEntity<BaseResponse<?>> searchPerformances(
            @UserId Long userId,
            @RequestParam(required = false) String aid,
            @RequestParam(required = false) Long pid,
            @RequestParam(required = false) String term
    ) {
        SearchedPerformancesDTO searchResult = setlistSearchFacade.searchPerformances(aid, pid, term);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
                SetlistSearchPerformancesResponse.from(searchResult));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/artist-musics")
    public ResponseEntity<BaseResponse<?>> searchArtistMusics(
            @UserId Long userId,
            @RequestParam(required = false) String aid,
            @RequestParam(required = false) String term,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int offset,
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        if (Objects.isNull(aid) && Objects.isNull(term)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        SetlistSearchArtistMusicsDTO artistMusics = setlistSearchFacade.searchArtistMusics(aid, term, offset, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, SetlistSearchArtistMusicsResponse.from(artistMusics));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/musics")
    public ResponseEntity<BaseResponse<?>> searchMusics(
            @UserId Long userId,
            @RequestParam String term,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int offset,
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        SetlistSearchMusicsDTO musics = setlistSearchFacade.searchMusics(term, offset, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, SetlistSearchMusicsResponse.from(musics));
    }
}
