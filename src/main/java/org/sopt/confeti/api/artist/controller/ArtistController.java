package org.sopt.confeti.api.artist.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.artist.dto.response.SearchArtistResponse;
import org.sopt.confeti.api.artist.facade.ArtistFacade;
import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;
import org.sopt.confeti.api.user.facade.UserInfoFacade;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistFacade artistFacade;

    @GetMapping
    public ResponseEntity<BaseResponse<?>> search(
            @RequestHeader(name = "Authorization", required = false) Long userId,
            @RequestParam(name = "search") String keyword
    ) {
        SearchArtistDTO artist = artistFacade.searchByKeyword(userId, keyword);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, SearchArtistResponse.from(artist));
    }
}
