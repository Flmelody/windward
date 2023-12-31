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

/**
 * @author esotericman
 */
public class MediaType {
  public static final String WEB_SOCKET_BINARY = "websocket/binary;";
  public static final String APPLICATION_JSON_VALUE = "application/json;charset=UTF-8";
  public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
  public static final String TEXT_PLAIN_VALUE = "text/plain;charset=UTF-8";
  public static final String TEXT_HTML_VALUE = "text/html;charset=UTF-8";

  private MediaType() {}
}
