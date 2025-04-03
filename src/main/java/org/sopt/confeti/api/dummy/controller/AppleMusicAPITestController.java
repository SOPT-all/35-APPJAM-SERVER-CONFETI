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

    @GetMapping("${api.endpoints.dummy.catalog-album-relationship}")
    public ResponseEntity<BaseResponse<?>> getCatalogAlbumRelationship(
            @RequestParam String id,
            @RequestParam String relationship,
            @RequestParam(required = false) List<String> include,
            @RequestParam(required = false) Integer limit
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogAlbumRelationship(id, relationship, include, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.catalog-album-relationship-view}")
    public ResponseEntity<BaseResponse<?>> getCatalogAlbumRelationshipView(
            @RequestParam String id,
            @RequestParam String view,
            @RequestParam(required = false) List<String> include,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String with
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogAlbumRelationshipView(id, view, include, limit, with);
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

    @GetMapping("${api.endpoints.dummy.catalog-artist-relationship}")
    public ResponseEntity<BaseResponse<?>> getCatalogArtistRelationship(
            @RequestParam String id,
            @RequestParam String relationship,
            @RequestParam(required = false) List<String> include,
            @RequestParam(required = false) Integer limit
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogArtistRelationship(id, relationship, include, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.catalog-artist-relationship-view}")
    public ResponseEntity<BaseResponse<?>> getCatalogArtistRelationshipView(
            @RequestParam String id,
            @RequestParam String view,
            @RequestParam(required = false) List<String> include,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String with
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogArtistRelationshipView(id, view, include, limit, with);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.catalog-song}")
    public ResponseEntity<BaseResponse<?>> getCatalogSong(
            @RequestParam String id,
            @RequestParam(required = false) List<String> include
    ) {
        Object response = appleMusicAPITestFacade.requestCatalogSong(id, include);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.multiple-catalog-songs}")
    public ResponseEntity<BaseResponse<?>> getMultipleCatalogSongs(
            @RequestParam String ids,
            @RequestParam(required = false) List<String> include
    ) {
        Object response = appleMusicAPITestFacade.requestMultipleCatalogSongs(ids, include);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.search-catalog-resources}")
    public ResponseEntity<BaseResponse<?>> getSearchResult(
            @RequestParam String term,
            @RequestParam List<String> types,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String offset
    ) {
        Object response = appleMusicAPITestFacade.requestSearch(term, types, limit, offset);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.catalog-search-hints}")
    public ResponseEntity<BaseResponse<?>> getSearchHintsResult(
            @RequestParam String term,
            @RequestParam(required = false) Integer limit
    ) {
        Object response = appleMusicAPITestFacade.requestSearchHints(term, limit);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }

    @GetMapping("${api.endpoints.dummy.catalog-search-suggestions}")
    public ResponseEntity<BaseResponse<?>> getSearchSuggestionsResult(
            @RequestParam List<String> kinds,
            @RequestParam(required = false) Integer limit,
            @RequestParam String term,
            @RequestParam(required = false) List<String> types
    ) {
        Object response = appleMusicAPITestFacade.requestSearchSuggestions(kinds, limit, term, types);
        return ApiResponseUtil.success(SuccessMessage.SUCCESS, response);
    }
}
