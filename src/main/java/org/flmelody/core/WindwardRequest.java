/*
 * Copyright (C) 2023 Flmelody.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flmelody.core;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author esotericman
 */
public class WindwardRequest implements RequestReader {
  private String method;
  private String uri;
  private Boolean keepAlive;
  private final Map<String, List<String>> headers = new HashMap<>(2 << 3);
  private final Map<String, List<String>> querystring = new HashMap<>(2 << 3);
  private final Map<String, Object> pathVariables = new HashMap<>(2 << 3);
  private String requestBody;
  private final RequestReader requestReader = new DefaultRequestReader();

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
   * return request header
   *
   * @param name headers name
   * @return headers values
   */
  public List<String> getHeader(String name) {
    if (!headers.containsKey(name)) {
      return Collections.emptyList();
    }
    return headers.get(name);
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
   * get request querystring
   *
   * @return querystring or empty
   */
  public Map<String, List<String>> getQuerystring() {
    return querystring;
  }

  /**
   * get path variables
   *
   * @return path variables
   */
  public Map<String, Object> getPathVariables() {
    return pathVariables;
  }

  /**
   * get request body
   *
   * @return request body or null
   */
  public String getRequestBody() {
    return requestBody;
  }

  /** {@inheritDoc} */
  @Override
  public <I> I readJson(String body, Class<I> clazz) {
    return requestReader.readJson(body, clazz);
  }

  /** {@inheritDoc} */
  @Override
  public <I> I readJson(String body, Type type) {
    return requestReader.readJson(body, type);
  }

  /** {@inheritDoc} */
  @Override
  public <I> I bindJson(String body, Class<I> clazz, Class<?>... groups) {
    return requestReader.bindJson(body, clazz, groups);
  }

  /** {@inheritDoc} */
  @Override
  public <I> I bindJson(String body, Type type, Class<?>... groups) {
    return requestReader.bindJson(body, type, groups);
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

    public WindwardRequestBuilder headers(Map<String, List<String>> headers) {
      windwardRequest.headers.putAll(headers);
      return this;
    }

    public WindwardRequestBuilder querystring(Map<String, List<String>> querystring) {
      windwardRequest.querystring.putAll(querystring);
      return this;
    }

    public WindwardRequestBuilder pathVariables(Map<String, Object> pathVariables) {
      windwardRequest.pathVariables.putAll(pathVariables);
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
