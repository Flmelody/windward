/*
 * Copyright (C) 2023 Flmelody and original author or authors.
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
 * Common http headers values
 *
 * @author esotericman
 */
public final class HttpHeaderValue {
  /** {@code "application/json"} */
  public static final String APPLICATION_JSON = "application/json";
  /** {@code "application/x-www-form-urlencoded"} */
  public static final String APPLICATION_X_WWW_FORM_URLENCODED =
      "application/x-www-form-urlencoded";
  /** {@code "application/octet-stream"} */
  public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  /** {@code "application/xhtml+xml"} */
  public static final String APPLICATION_XHTML = "application/xhtml+xml";
  /** {@code "application/xml"} */
  public static final String APPLICATION_XML = "application/xml";
  /** {@code "application/zstd"} */
  public static final String APPLICATION_ZSTD = "application/zstd";
  /** {@code "attachment"} */
  public static final String ATTACHMENT = "attachment";
  /** {@code "base64"} */
  public static final String BASE64 = "base64";
  /** {@code "binary"} */
  public static final String BINARY = "binary";
  /** {@code "boundary"} */
  public static final String BOUNDARY = "boundary";
  /** {@code "bytes"} */
  public static final String BYTES = "bytes";
  /** {@code "charset"} */
  public static final String CHARSET = "charset";
  /** {@code "chunked"} */
  public static final String CHUNKED = "chunked";
  /** {@code "close"} */
  public static final String CLOSE = "close";
  /** {@code "compress"} */
  public static final String COMPRESS = "compress";
  /** {@code "100-continue"} */
  public static final String CONTINUE = "100-continue";
  /** {@code "deflate"} */
  public static final String DEFLATE = "deflate";
  /** {@code "x-deflate"} */
  public static final String X_DEFLATE = "x-deflate";
  /** {@code "file"} */
  public static final String FILE = "file";
  /** {@code "filename"} */
  public static final String FILENAME = "filename";
  /** {@code "form-data"} */
  public static final String FORM_DATA = "form-data";
  /** {@code "gzip"} */
  public static final String GZIP = "gzip";
  /** {@code "br"} */
  public static final String BR = "br";
  /** {@code "snappy"} */
  public static final String SNAPPY = "snappy";
  /** {@code "zstd"} */
  public static final String ZSTD = "zstd";
  /** {@code "gzip,deflate"} */
  public static final String GZIP_DEFLATE = "gzip,deflate";
  /** {@code "x-gzip"} */
  public static final String X_GZIP = "x-gzip";
  /** {@code "identity"} */
  public static final String IDENTITY = "identity";
  /** {@code "keep-alive"} */
  public static final String KEEP_ALIVE = "keep-alive";
  /** {@code "max-age"} */
  public static final String MAX_AGE = "max-age";
  /** {@code "max-stale"} */
  public static final String MAX_STALE = "max-stale";
  /** {@code "min-fresh"} */
  public static final String MIN_FRESH = "min-fresh";
  /** {@code "multipart/form-data"} */
  public static final String MULTIPART_FORM_DATA = "multipart/form-data";
  /** {@code "multipart/mixed"} */
  public static final String MULTIPART_MIXED = "multipart/mixed";
  /** {@code "must-revalidate"} */
  public static final String MUST_REVALIDATE = "must-revalidate";
  /** {@code "name"} */
  public static final String NAME = "name";
  /** {@code "no-cache"} */
  public static final String NO_CACHE = "no-cache";
  /** {@code "no-store"} */
  public static final String NO_STORE = "no-store";
  /** {@code "no-transform"} */
  public static final String NO_TRANSFORM = "no-transform";
  /** {@code "none"} */
  public static final String NONE = "none";
  /** {@code "0"} */
  public static final String ZERO = "0";
  /** {@code "only-if-cached"} */
  public static final String ONLY_IF_CACHED = "only-if-cached";
  /** {@code "private"} */
  public static final String PRIVATE = "private";
  /** {@code "proxy-revalidate"} */
  public static final String PROXY_REVALIDATE = "proxy-revalidate";
  /** {@code "public"} */
  public static final String PUBLIC = "public";
  /** {@code "quoted-printable"} */
  public static final String QUOTED_PRINTABLE = "quoted-printable";
  /** {@code "s-maxage"} */
  public static final String S_MAXAGE = "s-maxage";
  /** {@code "text/css"} */
  public static final String TEXT_CSS = "text/css";
  /** {@code "text/html"} */
  public static final String TEXT_HTML = "text/html";
  /** {@code "text/event-stream"} */
  public static final String TEXT_EVENT_STREAM = "text/event-stream";
  /** {@code "text/plain"} */
  public static final String TEXT_PLAIN = "text/plain";
  /** {@code "trailers"} */
  public static final String TRAILERS = "trailers";
  /** {@code "upgrade"} */
  public static final String UPGRADE = "upgrade";
  /** {@code "websocket"} */
  public static final String WEBSOCKET = "websocket";
  /** {@code "XmlHttpRequest"} */
  public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

  private HttpHeaderValue() {}
}
