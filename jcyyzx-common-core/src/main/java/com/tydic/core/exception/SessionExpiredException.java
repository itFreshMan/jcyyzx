package com.tydic.core.exception;

/**
 * Created by qxjiao on 2018/1/29.
 *
 * @author jiaoqx
 */
public class SessionExpiredException extends RuntimeException {

  private static final long serialVersionUID = -1107631170534840303L;

  public SessionExpiredException(String message) {
    super(message);
  }

  public SessionExpiredException(Throwable cause) {
    super(cause);
  }

  public SessionExpiredException(String message, Throwable cause) {
    super(message, cause);
  }
}
