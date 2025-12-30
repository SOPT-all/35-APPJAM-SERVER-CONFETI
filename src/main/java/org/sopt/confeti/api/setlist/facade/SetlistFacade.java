package org.sopt.confeti.api.setlist.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.api.setlist.dto.response.GetSetlistDetailResponse;
import org.sopt.confeti.api.setlist.dto.response.GetSetlistDetailResponse_deprecated;
import org.sopt.confeti.api.setlist.dto.response.SetlistSummaryResponse;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistAddSongDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistCreateRequestDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistUpdateSongOrderDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.SetlistAddSongResponseDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.SetlistCreateResponseDTO;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.setlist.SetlistSortTypeDeprecated;
import org.sopt.confeti.domain.setlist.application.SetlistEditService;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.interceptor.auth.UserContext;

@Facade
@RequiredArgsConstructor
public class SetlistFacade {

    private final SetlistService setlistService;
    private final SetlistEditService setlistEditService;
    private final UserService userService;

    @Deprecated
    public GetAllSetlistsResponse getAllMySetlists_deprecated(Long userId, String sortBy) {
        SetlistSortTypeDeprecated sortType = SetlistSortTypeDeprecated.from(sortBy);
        return setlistService.getAllMySetlists_deprecated(userId, sortType);
    }

    public GetAllSetlistsResponse getAllMySetlists(SetlistSortType sortBy) {
        return setlistService.getAllMySetlists(UserContext.get().id(), sortBy);
    }

    public List<SetlistSummaryResponse> getPreviewMySetlists_deprecated(Long userId) {
        return setlistService.getPreviewMySetlists_deprecated(userId);
    }

    public List<SetlistSummaryResponse> getPreviewMySetlists() {
        return setlistService.getPreviewMySetlists(UserContext.get().id());
    }

    public SetlistCreateResponseDTO createSetLists(List<SetlistCreateRequestDTO> requests) {
        User user = userService.findById(UserContext.get().id());
        return new SetlistCreateResponseDTO(setlistService.createSetLists(user, requests));
    }

    public SetlistAddSongResponseDTO addSongs(Long setlistId,
        List<SetlistAddSongDTO> requests) {
        return new SetlistAddSongResponseDTO(
            setlistService.addSongs(UserContext.get().id(), setlistId, requests));
    }

    @Deprecated
    public GetSetlistDetailResponse_deprecated getSetlistDetail_deprecated(Long userId,
        Long setlistId) {
        return setlistService.getSetlistDetail_deprecated(userId, setlistId);
    }

    public GetSetlistDetailResponse getSetlistDetail(Long setlistId) {
        return setlistService.getSetlistDetail(UserContext.get().id(), setlistId);
    }

    public void startEdit(Long setlistId) {
        setlistEditService.startEdit(UserContext.get().id(), setlistId);
    }

    public void updateSongOrder_deprecated(Long userId, Long setlistId,
        List<SetlistUpdateSongOrderDTO> requests) {
        setlistEditService.updateSongOrder(userId, setlistId, requests);
    }

    public void updateSongOrder(Long setlistId, List<SetlistUpdateSongOrderDTO> requests) {
        setlistEditService.updateSongOrder(UserContext.get().id(), setlistId, requests);
    }

    public String deleteSong_deprecated(Long userId, Long setlistId, int orders) {
        return setlistEditService.deleteSong(userId, setlistId, orders);
    }

    public String deleteSong(Long setlistId, int orders) {
        return setlistEditService.deleteSong(UserContext.get().id(), setlistId, orders);
    }

    public void completeEdit(Long setlistId) {
        setlistEditService.completeEdit(UserContext.get().id(), setlistId);
    }

    public void cancelEdit(Long setlistId) {
        setlistEditService.cancelEdit(UserContext.get().id(), setlistId);
    }
}
