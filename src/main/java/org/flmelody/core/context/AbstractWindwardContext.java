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

package org.flmelody.core.context;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.Windward;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.exception.WindwardException;
import org.flmelody.core.plugin.json.JsonPlugin;
import org.flmelody.core.plugin.view.AbstractViewPlugin;

/**
 * @author esotericman
 */
public class AbstractWindwardContext implements WindwardContext {
  private final WindwardRequest windwardRequest;
  private final WindwardResponse windwardResponse;
  private Boolean closed = Boolean.FALSE;

  protected AbstractWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
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
  @Override
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
  @Override
  public List<String> getRequestParameters(String parameterName) {
    return this.windwardRequest.getQuerystring().get(parameterName);
  }

  /**
   * get path variables
   *
   * @return path variables
   */
  @Override
  public Map<String, Object> getPathVariables() {
    return this.windwardRequest.getPathVariables();
  }

  /**
   * get request body
   *
   * @return request body
   */
  @Override
  public String getRequestBody() {
    return this.windwardRequest.getRequestBody();
  }

  /**
   * get windwardRequest
   *
   * @return windwardRequest
   */
  @Override
  public WindwardRequest windwardRequest() {
    return this.windwardRequest;
  }

  /** close context */
  @Override
  public void close() {
    this.closed = Boolean.TRUE;
  }

  /**
   * check if current context is already closed
   *
   * @return is closed
   */
  @Override
  public Boolean isClosed() {
    return this.closed;
  }

  /** {@inheritDoc} */
  @Override
  public <I> I readJson(Class<I> clazz) {
    return windwardRequest.readJson(windwardRequest.getRequestBody(), clazz);
  }

  /** {@inheritDoc} */
  @Override
  public <I> I readJson(Type type) {
    return windwardRequest.readJson(windwardRequest.getRequestBody(), type);
  }

  /** {@inheritDoc} */
  @Override
  public <I> I bindJson(Class<I> clazz, Class<?>... groups) {
    return windwardRequest.bindJson(windwardRequest.getRequestBody(), clazz, groups);
  }

  /** {@inheritDoc} */
  @Override
  public <I> I bindJson(Type type, Class<?>... groups) {
    return windwardRequest.bindJson(windwardRequest.getRequestBody(), type, groups);
  }

  /**
   * response json
   *
   * @param data data
   * @param <T> type
   */
  @Override
  public <T> void writeJson(T data) {
    writeJson(HttpStatus.OK.value(), data);
  }

  /**
   * response json
   *
   * @param code response code
   * @param data data
   * @param <T> type
   */
  @Override
  public <T> void writeJson(int code, T data) {
    windwardResponse.write(code, MediaType.APPLICATION_JSON_VALUE, data);
  }

  /**
   * response plain string
   *
   * @param data strings
   */
  @Override
  public void writeString(String data) {
    writeString(HttpStatus.OK.value(), data);
  }

  /**
   * response plain string
   *
   * @param code response code
   * @param data strings
   */
  @Override
  public void writeString(int code, String data) {
    windwardResponse.write(code, MediaType.TEXT_PLAIN_VALUE, data);
  }

  /**
   * redirect
   *
   * @param redirectUrl location for redirecting
   */
  @Override
  public void redirect(String redirectUrl) {
    redirect(HttpStatus.FOUND.value(), redirectUrl);
  }

  /**
   * redirect
   *
   * @param code http code of redirecting
   * @param redirectUrl location for redirecting
   */
  @Override
  public void redirect(int code, String redirectUrl) {
    if (HttpStatus.MOVED_PERMANENTLY.value() == code || HttpStatus.FOUND.value() == code) {
      HashMap<String, Object> headerMap = new HashMap<>();
      headerMap.put("location", redirectUrl);
      windwardResponse.write(code, MediaType.TEXT_PLAIN_VALUE, headerMap, null);
      return;
    }
    throw new WindwardException("Illegal redirecting code" + code);
  }

  @Override
  public <M> void html(String viewUrl, M model) {
    if (viewUrl == null || viewUrl.isEmpty()) {
      throw new WindwardException("View name is empty!");
    }
    String extension;
    int i = viewUrl.lastIndexOf(".");
    if (i > 0) {
      extension = viewUrl.substring(i + 1);
    } else {
      throw new WindwardException("Unknown View extension!");
    }
    Optional<AbstractViewPlugin> view =
        Windward.plugins(AbstractViewPlugin.class).stream()
            .filter(viewPlugin -> viewPlugin.supportedExtension(extension))
            .findFirst();
    if (view.isPresent()) {
      AbstractViewPlugin viewPlugin = view.get();
      try {
        String renderedView =
            viewPlugin.resolveView(
                viewUrl, Windward.plugin(JsonPlugin.class).toObject(model, HashMap.class));
        windwardResponse.write(
            HttpStatus.OK.value(), MediaType.TEXT_HTML_VALUE, Collections.emptyMap(), renderedView);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new WindwardException("Unsupported View extension!");
    }
  }
}
