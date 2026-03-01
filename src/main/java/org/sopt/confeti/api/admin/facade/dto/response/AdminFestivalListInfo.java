package org.sopt.confeti.api.admin.facade.dto.response;

import java.util.List;

public record AdminFestivalListInfo(
    List<AdminFestivalPreviewInfo> upcomingFestivals,
    List<AdminFestivalPreviewInfo> finishedFestivals
) {

    public static AdminFestivalListInfo of(
        List<AdminFestivalPreviewInfo> upcomingFestivals,
        List<AdminFestivalPreviewInfo> finishedFestivals
    ) {
        return new AdminFestivalListInfo(upcomingFestivals, finishedFestivals);
    }
}
