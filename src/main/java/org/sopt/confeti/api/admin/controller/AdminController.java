package org.sopt.confeti.api.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.admin.controller.docs.AdminControllerDocs;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.api.admin.facade.AdminFacade;
import org.sopt.confeti.global.annotation.Admin;
import org.sopt.confeti.global.common.BaseResponse;
import org.sopt.confeti.global.message.SuccessMessage;
import org.sopt.confeti.global.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Admin
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController implements AdminControllerDocs {

    private final AdminFacade adminFacade;

    @Override
    @PostMapping("/ticket-vendors")
    public ResponseEntity<BaseResponse<TicketVendorResponse>> createTicketVendor(
        @Valid @RequestBody CreateTicketVendorRequest request
    ) {
        return ApiResponseUtil.success(
            SuccessMessage.CREATED, 
            adminFacade.createTicketVendor(request)
        );
    }
}
