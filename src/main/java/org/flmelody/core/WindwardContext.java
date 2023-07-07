package org.flmelody.core;

import java.util.List;

/**
 * http context
 *
 * @author esotericman
 */
public class WindwardContext {
  private final WindwardRequest windwardRequest;
  private final WindwardResponse windwardResponse;
  private Boolean closed = Boolean.FALSE;

  public WindwardContext(WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    this.windwardRequest = windwardRequest;
    this.windwardResponse = windwardResponse;
  }

  /**
   * get parameter by name and type
   *
   * @param parameterName parameterName
   * @param <P> class type
   * @return parameter
   */
  public <P> P getRequestParameter(String parameterName) {
    List<String> parameters = this.windwardRequest.getQuerystring().get(parameterName);
    if (parameters == null || parameters.isEmpty()) {
      return null;
    }
    //noinspection unchecked
    return (P) parameters.get(0);
  }

  /**
   * get parameter as list
   *
   * @param parameterName parameterName
   * @return parameters list
   */
  public List<String> getRequestParameters(String parameterName) {
    return this.windwardRequest.getQuerystring().get(parameterName);
  }

  /**
   * get request body
   *
   * @return request body
   */
  public String getRequestBody() {
    return this.windwardRequest.getRequestBody();
  }

  /**
   * get windwardRequest
   *
   * @return windwardRequest
   */
  public WindwardRequest windwardRequest() {
    return this.windwardRequest;
  }

  /** close context */
  public void close() {
    this.closed = Boolean.TRUE;
  }

  /**
   * check if current context is already closed
   *
   * @return is closed
   */
  public Boolean isClosed() {
    return this.closed;
  }

  /**
   * response json
   *
   * @param data data
   * @param <T> type
   */
  public <T> void json(T data) {
    json(HttpStatus.OK.value(), data);
  }

  /**
   * response json
   *
   * @param code response code
   * @param data data
   * @param <T> type
   */
  public <T> void json(int code, T data) {
    windwardResponse.write(code, MediaType.APPLICATION_JSON_VALUE, data);
  }

  /**
   * response plain string
   *
   * @param data strings
   */
  public void string(String data) {
    string(HttpStatus.OK.value(), data);
  }

  /**
   * response plain string
   *
   * @param code response code
   * @param data strings
   */
  public void string(int code, String data) {
    windwardResponse.write(code, MediaType.TEXT_PLAIN_VALUE, data);
  }
}
