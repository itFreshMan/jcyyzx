package com.tydic.core.util.network;

import com.tydic.core.util.Reflections;
import com.tydic.core.util.Strings;
import com.tydic.core.util.collection.Lists;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A utility which helps to exchange information with a server as a client.
 * Cookie or session is not supported.
 *
 * @author Guang YANG
 * @version 1.1
 */
public class RestClient {

  private RestTemplate connection;
  private String url;
  private HttpHeaders headers;
  private List<MediaType> mediaTypes;
  private MultiValueMap<String, Object> data;

  /**
   * Create a new <code>RestClient</code> instance by default timeout and retry strategy.
   */
  public static RestClient newClient() {
    return RestClient.newClient(10000, 5);
  }

  /**
   * Create a new <code>RestClient</code> instance using specific timeout and retry strategy.
   */
  public static RestClient newClient(int timeout, int retryCount) {
    RequestConfig config = RequestConfig.custom()
        .setConnectionRequestTimeout(timeout)
        .setConnectTimeout(timeout)
        .setSocketTimeout(timeout * 3)
        .build();
    HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(retryCount, false);
    HttpClient httpClient = HttpClientBuilder.create()
        .setDefaultRequestConfig(config)
        .setRetryHandler(retryHandler)
        .build();
    ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    RestTemplate connection = new RestTemplate(requestFactory);
    return new RestClient(connection);
  }

  private RestClient(RestTemplate connection) {
    this.connection = connection;
    this.headers = new HttpHeaders();
    this.mediaTypes = new ArrayList<>();
    this.data = new LinkedMultiValueMap<>();
  }

  /**
   * Config the url which you want to visit.
   */
  public RestClient url(String url) {
    this.url = url;
    return this;
  }

  /**
   * Declare <code>MediaType</code> you want to receive.<br>
   * Can be called multiple times.
   */
  public RestClient accept(MediaType mediaType) {
    this.mediaTypes.add(mediaType);
    return this;
  }

  /**
   * Add HTTP head to your request.<br>
   * Can be called multiple times.
   */
  public RestClient addHeader(String name, String value) {
    this.headers.add(name, value);
    return this;
  }

  /**
   * Add data to your request.<br>
   * Either instance of <code>java.util.Map</code> or POJO is acceptable.<br>
   * Can be called multiple times.
   */
  public RestClient addData(Object data) {
    if (data == null) {
      return this;
    }
    // convert data to java.util.Map if it is not
    if (!(data instanceof Map)) {
      data = Reflections.populate(data);
    }
    Map<?, ?> map = (Map) data;
    map.forEach((k, v) -> {
      String key = Strings.of(k);
      if (this.data.containsKey(key)) {
        this.data.get(key).add(v);
      } else {
        this.data.put(key, Lists.of(v));
      }
    });
    return this;
  }

  /**
   * Send your request using HTTP GET method.
   * @param responseType type of the response
   */
  public <T> ResponseEntity<T> get(Class<T> responseType) throws RestClientException {
    this.headers.setAccept(this.mediaTypes);
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.url);
    this.data.forEach(builder::queryParam);
    return this.connection.exchange(
        builder.build().encode().toUri(),
        HttpMethod.GET,
        new HttpEntity<>(this.headers),
        responseType);
  }

  /**
   * Send your request using HTTP POST method.
   * @param responseType type of the response
   */
  public <T> ResponseEntity<T> post(Class<T> responseType) throws RestClientException {
    this.headers.setAccept(this.mediaTypes);
    return this.connection.exchange(
        this.url,
        HttpMethod.POST,
        new HttpEntity<>(this.data, this.headers),
        responseType);
  }

}
