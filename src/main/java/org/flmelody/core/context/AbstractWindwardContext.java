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

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public abstract class AbstractWindwardContext implements WindwardContext {
  private static final Logger logger = LoggerFactory.getLogger(AbstractWindwardContext.class);
  protected final WindwardRequest windwardRequest;
  protected final WindwardResponse windwardResponse;
  protected final AtomicBoolean alreadyDone = new AtomicBoolean(false);
  private final AtomicBoolean closed = new AtomicBoolean(false);

  protected AbstractWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    this.windwardRequest = windwardRequest;
    this.windwardResponse = windwardResponse;
  }

  /** {@inheritDoc} */
  @Override
  public <P> P getRequestParameter(String parameterName) {
    List<String> parameters = this.windwardRequest.getQuerystring().get(parameterName);
    if (parameters == null || parameters.isEmpty()) {
      return null;
    }
    //noinspection unchecked
    return (P) parameters.get(0);
  }

  /** {@inheritDoc} */
  @Override
  public List<String> getRequestParameters(String parameterName) {
    return this.windwardRequest.getQuerystring().get(parameterName);
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, Object> getPathVariables() {
    return this.windwardRequest.getPathVariables();
  }

  /** {@inheritDoc} */
  @Override
  public String getRequestBody() {
    return this.windwardRequest.getRequestBody();
  }

  /** {@inheritDoc} */
  @Override
  public WindwardRequest windwardRequest() {
    return this.windwardRequest;
  }

  /** {@inheritDoc} */
  @Override
  public void close() {
    if (closed.compareAndSet(false, true)) {
      windwardResponse.close();
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean isClosed() {
    return this.closed.get();
  }

  /** {@inheritDoc} */
  @Override
  public <T> void writeJson(T data) {
    writeJson(HttpStatus.OK.value(), data);
  }

  /** {@inheritDoc} */
  @Override
  public <T> void writeJson(int code, T data) {
    write(code, MediaType.APPLICATION_JSON_VALUE.value, data);
  }

  /** {@inheritDoc} */
  @Override
  public void writeString(String data) {
    writeString(HttpStatus.OK.value(), data);
  }

  /** {@inheritDoc} */
  @Override
  public void writeString(int code, String data) {
    write(code, MediaType.TEXT_PLAIN_VALUE.value, data);
  }

  /** {@inheritDoc} */
  @Override
  public <T> void write(String contentType, T data) {
    write(HttpStatus.OK.value(), contentType, data);
  }

  /** {@inheritDoc} */
  @Override
  public <T> void write(int code, String contentType, T data) {
    if (alreadyDone.compareAndSet(false, true)) {
      windwardResponse.write(code, contentType, data);
    } else {
      logger.atWarn().log("Failed to response more than once, Request already done");
    }
  }
}
