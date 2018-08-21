package com.tydic.core.plugin.spring.bean;

public interface ReturnCode {

  String SUCCESS = "200";
  String SUCCESS_TEXT = "成功";

  String BAD_REQUEST = "400";
  String BAD_REQUEST_TEXT = "错误的请求";

  String UNAUTHORIZED = "401";
  String UNAUTHORIZED_TEXT = "未认证";

  String INTERNAL_ERROR = "500";
  String INTERNAL_ERROR_TEXT = "服务器错误";

  String ILLEGAL_ARGUMENT_TEXT = "请求参数不正确";
}
