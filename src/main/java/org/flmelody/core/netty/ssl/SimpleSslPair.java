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

package org.flmelody.core.netty.ssl;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import org.flmelody.core.SslPair;
import org.flmelody.util.UrlUtil;

/**
 * Default implementation for {@link SslPair}. <strong>You must provide an X.509 certificate chain
 * file in PEM format and a PKCS#8 private key file in PEM format</strong>
 *
 * @see io.netty.handler.ssl.SslContextBuilder#forServer(File, File)
 * @author esotericman
 */
public class SimpleSslPair implements SslPair {
  protected InputStream certFile;
  protected InputStream keyFile;
  protected boolean forceSsl;

  protected SimpleSslPair() {}

  public static SimpleSslPairBuilder builder() {
    return new SimpleSslPairBuilder(new SimpleSslPair());
  }

  @Override
  public InputStream certFile() {
    return this.certFile;
  }

  @Override
  public InputStream keyFile() {
    return this.keyFile;
  }

  @Override
  public boolean forceStatus() {
    return this.forceSsl;
  }

  /** Builder for SimpleSslPair */
  public static class SimpleSslPairBuilder {
    private final SimpleSslPair sslContext;

    private SimpleSslPairBuilder(SimpleSslPair sslContext) {
      this.sslContext = sslContext;
    }

    public SimpleSslPairBuilder certFile(String pathname) {
      if (Objects.isNull(pathname)) {
        throw new IllegalArgumentException("CertFile must not be null");
      }
      if (pathname.startsWith(UrlUtil.SLASH)) {
        pathname = pathname.substring(1);
      }
      this.sslContext.certFile = this.getClass().getClassLoader().getResourceAsStream(pathname);
      return this;
    }

    public SimpleSslPairBuilder keyFile(String pathname) {
      if (Objects.isNull(pathname)) {
        throw new IllegalArgumentException("KeyFile must not be null");
      }
      if (pathname.startsWith(UrlUtil.SLASH)) {
        pathname = pathname.substring(1);
      }
      this.sslContext.keyFile = this.getClass().getClassLoader().getResourceAsStream(pathname);
      return this;
    }

    public SimpleSslPairBuilder forceSsl() {
      this.sslContext.forceSsl = true;
      return this;
    }

    public SimpleSslPair build() {
      if (Objects.isNull(sslContext.certFile) || Objects.isNull(sslContext.keyFile)) {
        throw new IllegalStateException("CertFile and keyFile are required!");
      }
      return sslContext;
    }
  }
}
