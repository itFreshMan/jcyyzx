package com.tydic.core.util.network;

import javax.servlet.http.HttpServletRequest;

import static com.tydic.core.util.Strings.INSTANTIATION_PROHIBITED;

/**
 * @author Guang YANG
 * @version 1.0
 */
public final class IPUtils {

  private IPUtils() {
    throw new AssertionError(INSTANTIATION_PROHIBITED);
  }

  /**
   * Get IP address of remote host from Spring context.
   *
   * @return IP address of remote host
   */
  public static String getRemoteHost() {
    HttpServletRequest request = RequestUtils.getRequest();
    return IPUtils.getRemoteHost(request);
  }

  /**
   * Get IP address of remote host from HttpServletRequest.
   *
   * @param request HttpServletRequest
   * @return IP address of remote host
   */
  public static String getRemoteHost(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}