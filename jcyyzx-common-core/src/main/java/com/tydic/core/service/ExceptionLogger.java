package com.tydic.core.service;

import org.springframework.scheduling.annotation.Async;

/**
 * @author qxjiao
 * @date 2018/5/24
 * 异常日志接口，在加@Loggable注解的业务类且exceptional为true时，会插入业务异常日志
 */
@Async
public interface ExceptionLogger {

  String RUNTIME = "R";
  String SERVICE = "S";

  /**
   * 插入异常日志
   *
   * @param description 失败提示
   * @param throwable 异常
   * @param callee 业务方法路径
   * @param exceptionType 操作类型  S 业务异常 R 运行期异常
   * @param operationalId 操作日志id
   */
  void write(Throwable throwable, String description, String callee, String exceptionType, String operationalId);
}
