package org.sopt.confeti.api.dummy.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.dummy.dto.concert.CreateConcertRequest;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalRequest;
import org.sopt.confeti.api.dummy.dto.festival.FestivalStagesDTO;
import org.sopt.confeti.api.dummy.facade.DummyFacade;
import org.sopt.confeti.api.dummy.facade.dto.concert.ConcertFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.UploadConcertFilesDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.UploadFestivalFilesDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("${api.endpoints.dummy.base}")
public class DummyPageController {

    private final DummyFacade dummyFacade;

    @Value("${api.endpoints.dummy.base}")
    private String dummyPageBase;

    @Value("${api.endpoints.dummy.festival-page}")
    private String festivalDummyPage;

    @Value("${api.endpoints.dummy.concert-page}")
    private String concertDummyPage;

    @Value("${api.endpoints.dummy.apple-music-api-page}")
    private String appleMusicAPIPage;

    @GetMapping("${api.endpoints.dummy.festival-page}")
    public String getAddFestivalPage(Model model) {
        model.addAttribute("festival", new CreateFestivalRequest());
        model.addAttribute("actionUrl", dummyPageBase + festivalDummyPage);
        return "dummy/festival";
    }

    @PostMapping("${api.endpoints.dummy.festival-page}")
    public String createFestival(
            @Valid @ModelAttribute("festival") CreateFestivalRequest request,
            @RequestParam("posterFile") MultipartFile poster,
            @RequestParam("posterBgFile") MultipartFile posterBg,
            @RequestParam("logoFile") MultipartFile logo,
            @RequestParam(value = "reservationLogoFile", required = false) List<MultipartFile> reservationLogos,
            RedirectAttributes redirectAttributes
    ) {
        FestivalFilePathsDTO filePaths = dummyFacade.uploadFestivalFiles(
                UploadFestivalFilesDTO.of(poster, posterBg, logo, reservationLogos)
        );

        dummyFacade.createFestival(CreateFestivalDTO.of(request, filePaths));

        redirectAttributes.addFlashAttribute("message", "서버에 정상적으로 저장되었습니다.");

        return "redirect:" + dummyPageBase + festivalDummyPage;
    }

    @GetMapping("${api.endpoints.dummy.festival-fix-page}")
    public String getFixFestivalPage(
            @PathVariable Long festivalId,
            Model model
    ) {
        FestivalStagesDTO stagesDTO = dummyFacade.getFestivalStages(festivalId);
        model.addAttribute("stages", stagesDTO.stages());
        model.addAttribute("festivalId", festivalId);
        return "dummy/fix-festival";
    }

    @GetMapping("${api.endpoints.dummy.concert-page}")
    public String getAddConcertPage(Model model) {
        model.addAttribute("concert", new CreateConcertRequest());
        model.addAttribute("actionUrl", dummyPageBase + concertDummyPage);
        return "dummy/concert";
    }

    @PostMapping("${api.endpoints.dummy.concert-page}")
    public String createConcert(
            @Valid @ModelAttribute("concert") CreateConcertRequest request,
            @RequestParam("posterFile") MultipartFile poster,
            @RequestParam("posterBgFile") MultipartFile posterBg,
            @RequestParam(value = "reservationLogoFile", required = false) List<MultipartFile> reservationLogos,
            RedirectAttributes redirectAttributes
    ) {
        ConcertFilePathsDTO filePaths = dummyFacade.uploadConcertFiles(
                UploadConcertFilesDTO.of(poster, posterBg, reservationLogos)
        );

        dummyFacade.createConcert(CreateConcertDTO.of(request, filePaths));

        redirectAttributes.addFlashAttribute("message", "서버에 정상적으로 저장되었습니다.");

        return "redirect:" + dummyPageBase + concertDummyPage;
    }

    @GetMapping("${api.endpoints.dummy.apple-music-api-page}")
    public String getAppleMusicApiPage(Model model) {
        model.addAttribute("actionUrl", dummyPageBase + appleMusicAPIPage);
        return "dummy/appleMusicApiTest";
    }
}
