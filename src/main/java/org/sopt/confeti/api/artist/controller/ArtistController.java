package org.sopt.confeti.api.artist.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.artist.dto.response.SearchACArtistsResponse;
import org.sopt.confeti.api.artist.dto.response.SearchArtistResponse;
import org.sopt.confeti.api.artist.facade.ArtistFacade;
import org.sopt.confeti.api.artist.facade.dto.response.SearchACArtistsDTO;
import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.annotation.UserId;
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
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistFacade artistFacade;

    @Permission(role = {Role.GENERAL})
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<?>> search(
            @UserId(require = false) Long userId,
            @RequestParam(required = false) String term,
            @RequestParam(required = false) String aid
    ) {
        if (Objects.isNull(term) && Objects.isNull(aid)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        SearchArtistDTO artist = artistFacade.search(userId, term, aid);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, SearchArtistResponse.from(artist));
    }

    @GetMapping("/search/ac")
    public ResponseEntity<BaseResponse<?>> searchAutoComplete(
            @UserId(require = false) Long userId,
            @RequestParam String term,
            @RequestParam(required = false, defaultValue = "1") Integer limit
    ) {
        SearchACArtistsDTO artistsDTO = artistFacade.searchACArtists(term, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, SearchACArtistsResponse.from(artistsDTO));
    }
}
