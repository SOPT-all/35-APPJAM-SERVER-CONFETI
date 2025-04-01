package org.sopt.confeti.global.aop;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.sopt.confeti.global.annotation.RetryOnTokenExpire;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

@Aspect
@Component
public class RetryOnTokenExpireAspect {

    /**
     * RetryOnTokenExpire 어노테이션을 등록한 메서드에 자동으로 적용되는 Retry 패턴 실패 시 메서드의 클래스에 등록된 refreshToken() 함수를 호출해 토큰을 재생성 한 뒤 다시
     * 시도합니다. 재시도는 어노테이션의 maxRetries 값만큼 시도합니다.
     *
     * @param joinPoint 적용 대상
     * @return 성공 결과
     * @throws Throwable 401이 아닌 응답 코드
     */
    @Around("@annotation(org.sopt.confeti.global.annotation.RetryOnTokenExpire)")
    public Object retryOnTokenExpire(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RetryOnTokenExpire annotation = method.getAnnotation(RetryOnTokenExpire.class);
        int maxRetries = annotation.maxRetries();

        int attempts = 0;
        Object target = joinPoint.getTarget();

        while (attempts <= maxRetries) {
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                ++attempts;

                if (isTokenExpiredException(e) && attempts <= maxRetries) {
                    invokeRefreshToken(target);
                    continue;
                }

                throw e;
            }
        }

        // 여기까지 도달하지 못해요
        throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
    }

    /**
     * 401 응답 코드인지 확인하는 함수
     *
     * @param e Client 오류
     * @return 401 응답 코드 여부
     */
    private boolean isTokenExpiredException(Exception e) {
        return e instanceof UnauthorizedException
                && ((UnauthorizedException) e).getErrorMessage().getHttpStatus().value() == HttpStatusCode.UNAUTHORIZED;
    }

    /**
     * 어노테이션을 등록한 메서드의 클래스에 구현된 refreshToken() 함수를 호출해 토큰을 재발행하는 함수 리플렉션을 사용해 호출합니다.
     *
     * @param target 어노테이션을 등록한 메서드
     */
    private void invokeRefreshToken(Object target) {
        try {
            Method refreshTokenMethod = target.getClass().getDeclaredMethod("refreshToken");
            refreshTokenMethod.setAccessible(true);
            refreshTokenMethod.invoke(target);
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
