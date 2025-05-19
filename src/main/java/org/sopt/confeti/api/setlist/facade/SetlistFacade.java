package org.sopt.confeti.api.setlist.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.setlist.application.SetlistEditService;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistAddMusicDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistCreateDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistUpdateMusicOrderDTO;
import org.sopt.confeti.api.setlist.facade.dto.response.SetlistAddMusicResponse;
import org.sopt.confeti.api.setlist.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.api.setlist.dto.response.GetSetlistDetailResponse;
import org.sopt.confeti.api.setlist.facade.dto.response.SetlistCreateResponse;
import org.sopt.confeti.api.setlist.dto.response.SetlistSummaryDto;
import org.sopt.confeti.global.annotation.Facade;

@Facade
@RequiredArgsConstructor
public class SetlistFacade {

    private final SetlistService setlistService;
    private final SetlistEditService setlistEditService;

    public GetAllSetlistsResponse getAllMySetlists(Long userId, String sortBy) {
        SetlistSortType sortType = SetlistSortType.from(sortBy);
        return setlistService.getAllMySetlists(userId, sortType);
    }

    public List<SetlistSummaryDto> getPreviewMySetlists(Long userId) {
        return setlistService.getPreviewMySetlists(userId);
    }

    public SetlistCreateResponse createSetLists(Long userId, List<SetlistCreateDTO> requests) {
        return new SetlistCreateResponse(setlistService.createSetLists(userId, requests));
    }

    public SetlistAddMusicResponse addMusics(Long userId, Long setlistId, List<SetlistAddMusicDTO> requests) {
        return new SetlistAddMusicResponse(setlistService.addMusics(userId, setlistId, requests));
    }

    public GetSetlistDetailResponse getSetlistDetail(Long userId, Long setlistId) {
        return setlistService.getSetlistDetail(userId, setlistId);
    }

    public void startEdit(Long userId, Long setlistId) {
        setlistEditService.startEdit(userId, setlistId);
    }

    public void updateMusicOrder(Long userId, Long setlistId, List<SetlistUpdateMusicOrderDTO> requests) {
        setlistEditService.updateMusicOrder(userId, setlistId, requests);
    }

    public String deleteMusic(Long userId, Long setlistId, int orders) {
        return setlistEditService.deleteMusic(userId, setlistId, orders);
    }

    public void completeEdit(Long userId, Long setlistId) {
        setlistEditService.completeEdit(userId, setlistId);
    }

    public void cancelEdit(Long userId, Long setlistId) {
        setlistEditService.cancelEdit(userId, setlistId);
    }
}
