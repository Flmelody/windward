package org.flmelody.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author esotericman
 */
public class WindwardRequest {
  private String method;
  private String uri;
  private Boolean keepAlive;
  private String contentType;
  private final Map<String, List<String>> querystring = new HashMap<>();
  private String requestBody;

  public static WindwardRequestBuilder newBuild() {
    return new WindwardRequestBuilder(new WindwardRequest());
  }

  private WindwardRequest() {}

  /**
   * get request methods name
   *
   * @return methods name
   */
  public String getMethod() {
    return method;
  }

  /**
   * request uri
   *
   * @return request uri
   */
  public String getUri() {
    return uri;
  }

  /**
   * request contentType
   *
   * @return contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * get request querystring
   *
   * @return querystring or empty
   */
  public Map<String, List<String>> getQuerystring() {
    return querystring;
  }

  /**
   * get request body
   *
   * @return request body or null
   */
  public String getRequestBody() {
    return requestBody;
  }

  /** builder for WindwardRequest */
  public static class WindwardRequestBuilder {
    private final WindwardRequest windwardRequest;

    private WindwardRequestBuilder(WindwardRequest windwardRequest) {
      this.windwardRequest = windwardRequest;
    }

    public WindwardRequestBuilder method(String method) {
      windwardRequest.method = method;
      return this;
    }

    public WindwardRequestBuilder uri(String uri) {
      windwardRequest.uri = uri;
      return this;
    }

    public WindwardRequestBuilder keepAlive(Boolean keepAlive) {
      windwardRequest.keepAlive = keepAlive;
      return this;
    }

    public WindwardRequestBuilder contentType(String contentType) {
      windwardRequest.contentType = contentType;
      return this;
    }

    public WindwardRequestBuilder querystring(Map<String, List<String>> querystring) {
      windwardRequest.querystring.putAll(querystring);
      return this;
    }

    public WindwardRequestBuilder requestBody(String requestBody) {
      windwardRequest.requestBody = requestBody;
      return this;
    }

    public WindwardRequest build() {
      return windwardRequest;
    }
  }
}
