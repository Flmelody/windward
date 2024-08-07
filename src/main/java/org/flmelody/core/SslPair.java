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

import java.io.InputStream;
import org.flmelody.core.netty.ssl.SimpleSslPair;

/**
 * Implement this interface to provide SSL certificates, note that the file format needs to be
 * adapted to the current service implementation.
 *
 * @see SimpleSslPair
 * @author esotericman
 */
public interface SslPair {

  /**
   * Path to the certificate file.
   *
   * @return certificate file
   */
  InputStream certFile();

  /**
   * Path to the key file.
   *
   * @return key file
   */
  InputStream keyFile();

  /**
   * Force https status
   *
   * @return must or not
   */
  boolean forceStatus();
}
