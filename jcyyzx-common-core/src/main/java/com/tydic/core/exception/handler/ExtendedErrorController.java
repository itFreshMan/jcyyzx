package com.tydic.core.exception.handler;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Guang YANG
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ExtendedErrorController extends BasicErrorController implements ErrorController {

  public ExtendedErrorController(ErrorAttributes errorAttributes, ServerProperties properties, List<ErrorViewResolver> errorViewResolvers) {
    super(errorAttributes, properties.getError(), errorViewResolvers);
  }

  @Override
  @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    HttpStatus status = this.getStatus(request);
    response.setStatus(status.value());
    Map<String, Object> errorAttributes = this.getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
    ModelAndView modelAndView = this.resolveErrorView(request, response, status, errorAttributes);
    response.setStatus(status.value());
    if (modelAndView == null) {
      modelAndView = new ModelAndView();
      String errorPath = this.errorPath(status);
      InternalResourceView view = new InternalResourceView(errorPath);
      modelAndView.setView(view);
      modelAndView.setStatus(status);
    }
    return modelAndView;
  }

  @Override
  @RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    return super.error(request);
  }

  private String errorPath(HttpStatus status) {
    switch (status) {
      case NOT_FOUND:
        return "/404.html";
      case UNPROCESSABLE_ENTITY:
        return "/422.html";
      default:
        return "/500.html";
    }
  }

}
