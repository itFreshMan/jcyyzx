package com.tydic.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ViewNotFoundException
 *
 * @author Guang YANG
 * @version 1.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ViewNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6284968061499774118L;

  /**
   * Constructs a new <code>ViewNotFoundException</code> with
   * <code>No such view.</code> as its detail message.
   */
  public ViewNotFoundException() {
    super("No such view.");
  }

  /**
   * Constructs a new <code>ViewNotFoundException</code> with
   * the specified view name.
   *
   * @param viewName name of the view
   */
  public ViewNotFoundException(String viewName) {
    super("No such view: " + viewName);
  }

  /**
   * Constructs a new runtime exception with the specified
   * view name and cause.
   *
   * @param viewName name of the view
   * @param cause the cause
   */
  public ViewNotFoundException(String viewName, Throwable cause) {
    super("No such view: " + viewName, cause);
  }

  /**
   * Constructs a new <code>ViewNotFoundException</code> with
   * the specified cause and a detail message of
   * <tt>(cause==null ? null : cause.toString())</tt>.
   *
   * @param cause the cause
   */
  public ViewNotFoundException(Throwable cause) {
    super(cause);
  }
}
