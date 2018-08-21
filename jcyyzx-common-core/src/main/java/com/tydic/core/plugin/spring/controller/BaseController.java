package com.tydic.core.plugin.spring.controller;

import com.tydic.core.plugin.spring.bean.ReturnCode;
import com.tydic.core.util.collection.EnhancedMap;
import com.tydic.core.util.network.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class BaseController implements ReturnCode {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected <V> EnhancedMap<String, V> getParameters(HttpServletRequest request) {
    return RequestUtils.getParameters(request);
  }

  protected <V> EnhancedMap<String, V> getParameters() {
    return RequestUtils.getParameters();
  }

  protected String packageErrors(Errors errors) {
    return this.packageErrors(errors, "<br/>");
  }

  protected String packageErrors(Errors errors, String delimiter) {
    return errors
        .getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(delimiter));
  }
}
