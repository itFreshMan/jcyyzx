package com.tydic.core.exception.handler;

/**
 * 所有对于Exception的打印等处理，均应实现此接口并注册至Spring。
 *
 * @author Guang YANG
 */
public interface ExceptionPrinter {

  void print(ErrorInformation ei);
}
