package org.sopt.confeti.api.setlist.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchArtistSongsResponse;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchArtistSongsResponse_deprecated;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchPerformancesResponse;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchSongsResponse;
import org.sopt.confeti.api.setlist.dto.response.search.SetlistSearchSongsResponse_deprecated;
import org.sopt.confeti.api.setlist.facade.SetlistSearchFacade;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SearchPerformancesDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistSongsDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchSongsDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
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

    @Permission(role = {Role.GENERAL})
    @GetMapping("/performances")
    public ResponseEntity<BaseResponse<SetlistSearchPerformancesResponse>> searchPerformances(
        @RequestParam(required = false) String aid,
        @RequestParam(required = false) Long pid,
        @RequestParam(required = false) String term
    ) {
        SearchPerformancesDTO searchResult = setlistSearchFacade.searchPerformances(aid, pid, term);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            SetlistSearchPerformancesResponse.from(searchResult));
    }

    @Deprecated
    @Permission(role = {Role.GENERAL})
    @GetMapping("/artist-musics")
    public ResponseEntity<BaseResponse<SetlistSearchArtistSongsResponse_deprecated>> searchArtistMusics(
        @RequestParam(required = false) String aid,
        @RequestParam(required = false) String term,
        @RequestParam(required = false, defaultValue = "0") @Min(0) int offset,
        @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        if (Objects.isNull(aid) && Objects.isNull(term)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        SetlistSearchArtistSongsDTO artistMusics = setlistSearchFacade.searchArtistSongs(aid,
            term, offset, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            SetlistSearchArtistSongsResponse_deprecated.from(artistMusics));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/artist-songs")
    public ResponseEntity<BaseResponse<SetlistSearchArtistSongsResponse>> searchArtistSongs(
        @RequestParam(required = false) String aid,
        @RequestParam(required = false) String term,
        @RequestParam(required = false, defaultValue = "0") @Min(0) int offset,
        @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        if (Objects.isNull(aid) && Objects.isNull(term)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        SetlistSearchArtistSongsDTO artistSongs = setlistSearchFacade.searchArtistSongs(aid,
            term, offset, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            SetlistSearchArtistSongsResponse.from(artistSongs));
    }

    @Deprecated
    @Permission(role = {Role.GENERAL})
    @GetMapping("/musics")
    public ResponseEntity<BaseResponse<SetlistSearchSongsResponse_deprecated>> searchMusics(
        @RequestParam String term,
        @RequestParam(required = false, defaultValue = "0") @Min(0) int offset,
        @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        SetlistSearchSongsDTO musics = setlistSearchFacade.searchSongs(term, offset, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            SetlistSearchSongsResponse_deprecated.from(musics));
    }

    @Permission(role = {Role.GENERAL})
    @GetMapping("/songs")
    public ResponseEntity<BaseResponse<SetlistSearchSongsResponse>> searchSongs(
        @RequestParam String term,
        @RequestParam(required = false, defaultValue = "0") @Min(0) int offset,
        @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int limit
    ) {
        SetlistSearchSongsDTO songs = setlistSearchFacade.searchSongs(term, offset, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS,
            SetlistSearchSongsResponse.from(songs));
    }
}
