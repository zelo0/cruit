package com.project.cruit.aop;

import com.project.cruit.authentication.CurrentUser;
import com.project.cruit.authentication.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

// 세션이 null이 아닌 지 체크 = 로그인이 되어 있는 지 체크
// patch, post, delete api 호출 전에는 무조건 로그인 체크
@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.enabled:true}")
public class CheckingSessionAdvice {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchMapping() {
    }
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMapping() {
    }
    @Pointcut("@annotation(com.project.cruit.aop.annotation.CheckSessionNotNull)")
    public void apparentAnnotation() {}
    
    @Before("patchMapping() || postMapping() || deleteMapping() || apparentAnnotation() ")
    public void CheckingSession(JoinPoint point) throws Throwable {
        SessionUser sessionUser = null;
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (arg instanceof SessionUser) {
                sessionUser = (SessionUser) arg;
            }
        }
        log.info("checking session: " + point.getSignature());
        SessionUser.checkIsNull(sessionUser);
    }
}
