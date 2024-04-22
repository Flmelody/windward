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
public enum MediaType {
  WEB_SOCKET_BINARY("websocket", "binary", "websocket/binary;"),
  TEXT_EVENT_STREAM("text", "event-stream", "text/event-stream"),
  MULTIPART_FORM_DATA_VALUE("multipart", "form-data", "multipart/form-data"),
  APPLICATION_JSON_VALUE("application", "json", "application/json;charset=UTF-8"),
  APPLICATION_STREAM_VALUE("application", "octet-stream", "application/octet-stream;charset=UTF-8"),

  IMAGE_JPEG_VALUE("image", "jpeg", "image/jpeg"),
  IMAGE_PNG_VALUE("image", "png", "image/png"),
  IMAGE_AVIF_VALUE("image", "avif", "image/avif"),
  IMAGE_WEBP_VALUE("image", "webp", "image/webp"),
  IMAGE_SVG_VALUE("image", "svg+xml", "image/svg+xml"),
  IMAGE_TIFF_VALUE("image", "tiff", "image/tiff"),
  IMAGE_GIF_VALUE("image", "gif", "image/gif"),
  IMAGE_BMP_VALUE("image", "bpm", "image/bpm"),

  TEXT_PLAIN_VALUE("text", "plain", "text/plain;charset=UTF-8"),
  TEXT_HTML_VALUE("text", "html", "text/html;charset=UTF-8"),
  TEXT_JS_VALUE("text", "javascript", "text/javascript;charset=UTF-8"),
  TEXT_CSS_VALUE("text", "css", "text/css;charset=UTF-8"),
  TEXT_CSV_VALUE("text", "csv", "text/csv;charset=UTF-8"),
  TEXT_PDF_VALUE("text", "pdf", "text/pdf;charset=UTF-8");

  public final String type;
  public final String subtype;

  public final String value;

  MediaType(String type, String subtype, String value) {
    this.type = type;
    this.subtype = subtype;
    this.value = value;
  }

  public static MediaType detectMediaType(String type) {
    if (type == null) {
      return APPLICATION_STREAM_VALUE;
    }
    String baseType = null;
    for (MediaType mediaType : MediaType.values()) {
      if (mediaType.value.contains(type)) {
        return mediaType;
      }
      if (type.startsWith(mediaType.type)) {
        baseType = mediaType.type;
      }
    }
    if (TEXT_PLAIN_VALUE.type.equals(baseType)) {
      return TEXT_PLAIN_VALUE;
    } else {
      return APPLICATION_STREAM_VALUE;
    }
  }
}
