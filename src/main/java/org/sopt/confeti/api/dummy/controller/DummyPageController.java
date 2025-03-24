package org.sopt.confeti.api.dummy.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalRequest;
import org.sopt.confeti.api.dummy.facade.DummyFacade;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.UploadFestivalFilesDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/dummy/page")
public class DummyPageController {

    private final DummyFacade dummyFacade;

    @GetMapping("/festivals")
    public String getAddFestivalPage(Model model) {
        model.addAttribute("festival", new CreateFestivalRequest());
        return "dummy/festival";
    }

    @PostMapping("/festivals")
    public String createFestival(
            @Valid @ModelAttribute("festival") CreateFestivalRequest request,
            BindingResult bindingResult,
            @RequestParam("posterFile") MultipartFile poster,
            @RequestParam("posterBgFile")MultipartFile posterBg,
            @RequestParam("logoFile") MultipartFile logo,
            @RequestParam(value = "reservationLogoFile", required = false) List<MultipartFile> reservationLogos,
            RedirectAttributes redirectAttributes
    ) {
        FestivalFilePathsDTO filePaths = dummyFacade.uploadFestivalFiles(
                UploadFestivalFilesDTO.of(poster, posterBg, logo, reservationLogos));

        dummyFacade.createFestival(CreateFestivalDTO.of(request, filePaths));

        redirectAttributes.addFlashAttribute("message", "서버에 정상적으로 저장되었습니다.");

        return "redirect:/dummy/page/festivals";
    }
}
