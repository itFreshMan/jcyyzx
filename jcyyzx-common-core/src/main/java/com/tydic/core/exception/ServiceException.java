package com.tydic.core.exception;

public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = -1107631170534840303L;

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
