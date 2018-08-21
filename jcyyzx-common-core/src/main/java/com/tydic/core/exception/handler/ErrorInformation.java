package com.tydic.core.exception.handler;

import com.tydic.core.util.Strings;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ErrorInformation implements Serializable {

  private static final long serialVersionUID = 8350247689299742761L;

  private int code;
  private String text;
  private Throwable exception;

  public ErrorInformation(int code, String text, Throwable exception) {
    super();
    this.code = code;
    this.text = text;
    this.exception = exception;
  }

  public ErrorInformation(HttpStatus status, Throwable exception) {
    this(status.value(), status.getReasonPhrase(), exception);
  }

  public int getCode() {
    return code;
  }

  public String getText() {
    return text;
  }

  public String getMessage() {
    return this.exception.getMessage();
  }

  public String getDetail() {
    return Strings.of(this.exception);
  }

  public Throwable getException() {
    return exception;
  }

  @Override
  public String toString() {
    return "ErrorInformation{" +
        "code=" + code +
        ", text='" + text + '\'' +
        ", exception=" + getMessage() +
        '}';
  }
}
