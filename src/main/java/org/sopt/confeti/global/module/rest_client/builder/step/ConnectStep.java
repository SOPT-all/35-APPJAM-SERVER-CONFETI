package org.sopt.confeti.global.module.rest_client.builder.step;

import java.util.Map;

public interface ConnectStep {

    ResponseStep connect(Map<String, String> headers);
}
