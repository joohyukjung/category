package com.musinsa.category.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //BookService의 모든 메서드
    @Around("execution(public * com.musinsa.category.service.CategoryService.*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("start - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        Object result = pjp.proceed();
        logger.info("finished - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        return result;
    }

    @Before("execution(public * com.musinsa.category.service.CategoryService.getCategory(..))")
    public void beforeLogging() {
        logger.info("getCategory을 수행합니다.");
    }

    @After("execution(public * com.musinsa.category.service.CategoryService.getAllCategoriesV2(..))")
    public void afterLogging() {
        logger.info("getAllCategoriesV2 끝났습니다.");
    }

    @AfterReturning(value = "execution(public * com.musinsa.category.service.CategoryService.getCategory(..))", returning = "returnValue")
    public void getCategoryAfterReturningAdvice(Object returnValue) {
        logger.info("getCategory 값은 {} 입니다.", returnValue);
    }

    @AfterThrowing(value = "execution(public * com.musinsa.category.service.CategoryService.getCategory(..))", throwing = "exception")
    public void getCategoryAfterThrowingAdvice(Exception exception) {
        logger.info("getCategory ERROR({}) 발생", exception.getMessage());
    }
}
