package com.tydic.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Guang YANG
 */
@Aspect
@Component
@Order(1)
public class RestControllerAspect extends AbstractParameterLoggerAspect {

  @Around("@within(controller)")
  public Object around(ProceedingJoinPoint point, RestController controller) throws Throwable {
    return super.around(point);
  }

}
