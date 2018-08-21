package com.tydic.core.exception.handler;

import com.tydic.core.util.Strings;
import com.tydic.core.util.collection.Lists;
import com.tydic.core.util.network.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author Guang YANG
 */
@ControllerAdvice
public class WebExceptionHandler {

  private static String DEFAULT_ERROR_VIEW = "error";
  private static String DEFAULT_ERROR_ATTR_NAME = "error";

  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  private Set<ExceptionPrinter> printers;

  @ExceptionHandler(Exception.class)
  public Object serviceExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
    this.logger.error(ex.getMessage());
    ErrorInformation errorInformation;
    if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
      String text;
      ResponseStatus status = ex.getClass().getAnnotation(ResponseStatus.class);
      if (Strings.isNotEmpty(status.reason())) {
        text = status.reason();
      } else {
        text = status.code().getReasonPhrase();
      }
      errorInformation = this.error(status.code().value(), text, ex);
    } else {
      errorInformation = this.error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
    response.setStatus(errorInformation.getCode());
    if (RequestUtils.isAjax(request)) {
      return errorInformation;
    } else {
      return new ModelAndView(DEFAULT_ERROR_VIEW).addObject(DEFAULT_ERROR_ATTR_NAME, errorInformation);
    }
  }

  @Autowired(required = false)
  public void setPrinters(Set<ExceptionPrinter> printers) {
    this.printers = printers;
  }

  private ErrorInformation error(HttpStatus status, Exception ex) {
    return new ErrorInformation(status, ex);
  }

  private ErrorInformation error(int code, String text, Exception ex) {
    ErrorInformation ei = new ErrorInformation(code, text, ex);
    Lists.of(this.printers).forEach(printer ->
        CompletableFuture.runAsync(() -> {
          printer.print(ei);
        }).whenCompleteAsync((r, e) -> {
          if (e != null) {
            logger.error(e.getMessage(), e);
          }
        }));
    return ei;
  }

}