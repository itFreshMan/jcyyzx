package com.tydic.core.aop;

import com.tydic.core.exception.ServiceException;
import com.tydic.core.util.collection.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public abstract class AbstractParameterLoggerAspect {

  protected Object around(ProceedingJoinPoint point) throws Throwable {
    // obtaining essential meta information
    long startAt = System.currentTimeMillis();
    Class<?> clazz = point.getTarget().getClass();
    String methodName = point.getSignature().getName();
    String callee = clazz.getName() + "#" + methodName;
    Logger logger = LoggerFactory.getLogger(clazz);
    try {
      // executing task
      logger.info("Invoking [{}]", callee);
      logger.info("Parameters: " + Lists.of(point.getArgs()).toString());
      return point.proceed();
    } catch (ServiceException ex) {
      String message = MessageFormat.format("Invoking failed: {0}, caused by: {1}", callee, ex.getMessage());
      logger.error(message, ex);
      throw ex;
    } catch (Throwable throwable) {
      String message = MessageFormat.format("Invoking failed: {0}", callee);
      logger.error(message, throwable);
      throw throwable;
    } finally {
      logger.info("Finishing [{}], totally cost {} milliseconds.", callee, (System.currentTimeMillis() - startAt));
    }
  }

}
