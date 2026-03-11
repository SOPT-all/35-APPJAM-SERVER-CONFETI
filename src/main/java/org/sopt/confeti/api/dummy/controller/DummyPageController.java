package org.sopt.confeti.api.dummy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("${api.endpoints.dummy.base}")
@Profile(value = {"prod"})
public class DummyPageController {

    @Value("${api.endpoints.dummy.base}")
    private String dummyPageBase;

    @Value("${api.endpoints.dummy.apple-music-api-page}")
    private String appleMusicAPIPage;

    @GetMapping("${api.endpoints.dummy.apple-music-api-page}")
    public String getAppleMusicApiPage(Model model) {
        model.addAttribute("actionUrl", dummyPageBase + appleMusicAPIPage);
        return "dummy/appleMusicApiTest";
    }
}
