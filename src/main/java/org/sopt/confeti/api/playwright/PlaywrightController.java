package org.sopt.confeti.api.playwright;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/png")
@RequiredArgsConstructor
public class PlaywrightController {

    private final PlaywrightUseCase playwrightUseCase;

    @PostMapping("/generate")
    public ResponseEntity<Resource> generate(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @RequestBody PlaywrightInfo playwrightInfo
    ) {
        log.info("PlaywrightController.generate: Start generating PNG.");
        if (!accessToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        accessToken = accessToken.substring("Bearer ".length());

        PlaywrightCommand command = PlaywrightCommand.builder()
                .url(playwrightInfo.url())
                .accessToken(accessToken)
                .x(playwrightInfo.x())
                .y(playwrightInfo.y())
                .width(playwrightInfo.width())
                .height(playwrightInfo.height())
                .build();

        String filename = "output.pdf";
        byte[] pdfBytes = playwrightUseCase.generatePng(command);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build());
        headers.setContentLength(pdfBytes.length);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
