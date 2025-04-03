package org.sopt.confeti.api.dummy.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.dummy.facade.AppleMusicAPITestFacade;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoints.dummy.base}${api.endpoints.dummy.apple-music-api-page}")
public class AppleMusicAPITestController {

    private final AppleMusicAPITestFacade appleMusicAPITestFacade;

    @GetMapping("${api.endpoints.dummy.catalog-album}")
    public ResponseEntity<BaseResponse<?>> getCatalogAlbum(
            @RequestParam String id,
            @RequestParam(required = false) String views
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogAlbum(id, views);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }
}
