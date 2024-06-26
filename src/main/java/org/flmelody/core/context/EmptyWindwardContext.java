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
import org.flmelody.core.WindwardRequest;

/**
 * @author esotericman
 */
public class EmptyWindwardContext implements WindwardContext {
  @Override
  public <P> P getRequestParameter(String parameterName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getRequestParameters(String parameterName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, Object> getPathVariables() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getRequestBody() {
    throw new UnsupportedOperationException();
  }

  @Override
  public WindwardRequest windwardRequest() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isClosed() {
    return true;
  }

  @Override
  public <T> void write(int code, String contentType, T data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> void writeJson(int code, T data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeString(int code, String data) {
    throw new UnsupportedOperationException();
  }
}
