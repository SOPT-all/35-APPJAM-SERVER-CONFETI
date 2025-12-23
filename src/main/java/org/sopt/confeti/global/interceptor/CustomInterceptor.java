package org.sopt.confeti.global.interceptor;

public interface CustomInterceptor {

    default int order() {
        return 100; // 오버라이딩하지 않으면 순서를 고려하지 않는다.
    }
}
