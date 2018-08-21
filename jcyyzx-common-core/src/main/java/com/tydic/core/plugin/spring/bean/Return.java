package com.tydic.core.plugin.spring.bean;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * @author Guang YANG
 */
public class Return<T> implements ReturnCode, Serializable {

  private static final long serialVersionUID = 2403603140219847061L;

  private String code;
  private String message;
  private T data;

  public Return() {
  }

  public Return(T data) {
    this.data = data;
  }

  public static Return<?> success() {
    return Return.success(SUCCESS, SUCCESS_TEXT);
  }

  public static Return<?> success(String message) {
    return Return.success(SUCCESS, message);
  }

  public static Return<?> success(String code, String message) {
    return new Return<>()
        .setCode(code)
        .setMessage(message);
  }

  public static <T> Return<T> success(T data) {
    return Return.success(data, SUCCESS, SUCCESS_TEXT);
  }

  public static <T> Return<T> success(T data, String message) {
    return Return.success(data, SUCCESS, message);
  }

  public static <T> Return<T> success(T data, String code, String message) {
    return new Return<>(data)
        .setCode(code)
        .setMessage(message);
  }

  public static Return<?> failure() {
    return Return.failure(INTERNAL_ERROR, INTERNAL_ERROR_TEXT);
  }

  public static Return<?> failure(String message) {
    return Return.failure(INTERNAL_ERROR, message);
  }

  public static Return<?> failure(String code, String message) {
    return new Return<>()
        .setCode(code)
        .setMessage(message);
  }

  public String getCode() {
    return code;
  }

  public Return<T> setCode(String code) {
    this.code = code;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Return<T> setMessage(String message) {
    this.message = message;
    return this;
  }

  public T getData() {
    return data;
  }

  public Return<T> setData(T data) {
    this.data = data;
    return this;
  }

  public CompletableFuture complete() {
    return CompletableFuture.completedFuture(this);
  }

  @Override
  public String toString() {
    return "Return{" +
        "code='" + code + '\'' +
        ", message='" + message + '\'' +
        ", data=" + data +
        '}';
  }
}
