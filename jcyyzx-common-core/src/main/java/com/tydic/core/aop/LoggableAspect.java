package com.tydic.core.aop;

import com.tydic.core.annotation.Loggable;
import com.tydic.core.exception.ServiceException;
import com.tydic.core.service.ExceptionLogger;
import com.tydic.core.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Set;

/**
 * 记录日志切面
 *
 * @author Guang YANG
 */
@Aspect
@Component
public class LoggableAspect {

  private Set<ExceptionLogger> exceptionalLoggers;

  @Around("@annotation(loggable)")
  public Object around(ProceedingJoinPoint point, Loggable loggable) {
    // obtaining essential meta information
    Class<?> clazz = point.getTarget().getClass();
    String methodName = point.getSignature().getName();
    String methodDesc = Strings.isEmpty(loggable.desc()) ? loggable.value() : loggable.desc();
    String callee = clazz.getName() + "#" + methodName;
    String calleeName = Strings.isEmpty(methodDesc) ? "操作" : methodDesc;
    Logger logger = LoggerFactory.getLogger(clazz);
    try {
      // executing task
      return point.proceed();
    } catch (ServiceException ex) {
      String message = MessageFormat.format("{0}失败，原因是：{1}", calleeName, ex.getMessage());
      logger.error(message, ex);
      // writing exception information if requested
      if (loggable.exceptional() && !this.exceptionalLoggers.isEmpty()) {
        for (ExceptionLogger exceptionalLogger : exceptionalLoggers) {
          exceptionalLogger.write(ex, message, callee, ExceptionLogger.SERVICE, Strings.EMPTY);
        }
      }
      throw ex;
    } catch (Throwable throwable) {
      String message = MessageFormat.format("{0}失败", calleeName);
      logger.error(message, throwable);
      // writing exception information if requested
      for (ExceptionLogger exceptionalLogger : exceptionalLoggers) {
        exceptionalLogger.write(throwable, message, callee, ExceptionLogger.RUNTIME, Strings.EMPTY);
      }
      throw new ServiceException(message, throwable);
    } finally {
      // writing operation information if requested
      if (loggable.operational()) {
        // TODO 记录业务日志
      }
    }
  }

  @Autowired(required = false)
  public void setExceptionalLoggers(Set<ExceptionLogger> exceptionalLoggers) {
    this.exceptionalLoggers = exceptionalLoggers;
  }
}
