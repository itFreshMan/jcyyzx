package com.tydic.core.util.network;

import com.tydic.core.util.IO;
import com.tydic.core.util.Loop;
import com.tydic.core.util.Strings;
import com.tydic.core.util.collection.EnhancedMap;
import com.tydic.core.util.collection.Maps;
import com.tydic.core.util.json.JSON;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import static com.tydic.core.util.Strings.INSTANTIATION_PROHIBITED;

/**
 * @author Guang YANG
 * @version 1.1
 */
public final class RequestUtils {

  private RequestUtils() {
    throw new AssertionError(INSTANTIATION_PROHIBITED);
  }

  /**
   * Find out whether the specific HttpServletRequest is an Ajax request.
   *
   * @param request HttpServletRequest
   * @return true if it is an Ajax request
   */
  public static boolean isAjax(HttpServletRequest request) {
    String header = Objects.requireNonNull(request).getHeader("x-requested-with");
    // custom header, varies with your frontend component
    String customHeader = Objects.requireNonNull(request).getHeader("x-ajax-header");
    return "XMLHttpRequest".equalsIgnoreCase(header) || "yes".equals(customHeader);
  }

  /**
   * Get HttpServletRequest instance from Spring context.
   *
   * @return HttpServletRequest
   * @since 1.1
   */
  public static HttpServletRequest getRequest() {
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (servletRequestAttributes == null) {
      return null;
    }
    return servletRequestAttributes.getRequest();
  }

  public static String getPath() {
    HttpServletRequest request = RequestUtils.getRequest();
    return RequestUtils.getPath(request);
  }

  public static String getPath(HttpServletRequest request) {
    return new StringBuilder(100)
        .append(request.getScheme())
        .append("://")
        .append(request.getServerName())
        .append(":")
        .append(request.getServerPort())
        .append(request.getContextPath())
        .toString();
  }

  /**
   * Build a map of parameters from Spring context.
   *
   * @return A map of parameters
   */
  public static <V> EnhancedMap<String, V> getParameters() {
    HttpServletRequest request = RequestUtils.getRequest();
    return RequestUtils.getParameters(request);
  }

  /**
   * Build a map of parameters from HttpServletRequest.
   *
   * @param request HttpServletRequest
   * @return A map of parameters
   */
  @SuppressWarnings("unchecked")
  public static <V> EnhancedMap<String, V> getParameters(HttpServletRequest request) {
    EnhancedMap<String, Object> param = Maps.of();
    // Deal with application/x-www-form-urlencoded
    request.getParameterMap().forEach((k, v) -> {
      if (v != null) {
        if (v.length == 1) {
          param.put(k, Strings.trim(v[0]));
        } else if (v.length > 1) {
          String[] values = new String[v.length];
          Loop.forEach(v, (i, e) -> values[i] = Strings.trim(e));
          if (k.endsWith("[]")) {
            param.put(k.substring(0, k.length() - 2), values);
          } else {
            param.put(k, values);
          }
        }
      }
    });
    // Turn Key[Sub-key] into maps
    String regex = ".+\\[\\w+]";
    if (param.keySet().stream().anyMatch(key -> key.matches(regex))) {
      Map<String, Object> flatted = Maps.of();
      param.entrySet()
          .stream()
          .filter(entry -> entry.getKey().matches(regex))
          .forEach(entry -> {
            String key = entry.getKey();
            String outerKey = key.substring(0, key.indexOf('['));
            String innerKey = key.substring(key.indexOf('[') + 1, key.indexOf(']'));
            System.out.println(innerKey);
            flatted.putIfAbsent(outerKey, Maps.of());
            ((Map) flatted.get(outerKey)).put(innerKey, entry.getValue());
          });
      param.entrySet().removeIf(entry -> entry.getKey().matches(regex));
      param.putAll(flatted);
    }
    // Deal with application/json
    try (InputStream is = request.getInputStream()) {
      String content = IO.stringify(is);
      param.putAll(JSON.parseMap(content));
    } catch (Exception ignored) {
    }
    // Return result
    return (EnhancedMap<String, V>) param;
  }

  public static String getUserAgent(HttpServletRequest request) {
    return request.getHeader("user-agent");
  }
}
