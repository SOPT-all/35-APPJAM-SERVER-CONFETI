package org.sopt.confeti.api.dummy.controller;

import java.util.List;
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
            @RequestParam(required = false) String views,
            @RequestParam(required = false) List<String> include
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogAlbum(id, views, include);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.multiple-catalog-albums}")
    public ResponseEntity<BaseResponse<?>> getMultipleCatalogAlbums(
            @RequestParam String ids,
            @RequestParam(required = false) List<String> include
    ) {
        Object response = appleMusicAPITestFacade.requestMultipleCatalogAlbums(ids, include);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.catalog-artist}")
    public ResponseEntity<BaseResponse<?>> getCatalogArtist(
            @RequestParam String id,
            @RequestParam(required = false) String views,
            @RequestParam(required = false) List<String> include
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogArtist(id, views, include);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.multiple-catalog-artists}")
    public ResponseEntity<BaseResponse<?>> getMultipleCatalogArtists(
            @RequestParam String ids,
            @RequestParam(required = false) List<String> include
    ) {
        Object response = appleMusicAPITestFacade.requestMultipleCatalogArtists(ids, include);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }
}
