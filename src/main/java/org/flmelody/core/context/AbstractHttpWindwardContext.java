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
import java.util.Optional;
import org.flmelody.core.HttpHeader;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.Windward;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.exception.WindwardException;
import org.flmelody.core.plugin.json.JsonPlugin;
import org.flmelody.core.plugin.view.AbstractViewPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public abstract class AbstractHttpWindwardContext extends AbstractWindwardContext
    implements HttpBasicWindwardContext {
  private static final Logger logger = LoggerFactory.getLogger(AbstractHttpWindwardContext.class);

  protected AbstractHttpWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
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

  /** {@inheritDoc} */
  @Override
  public void redirect(String redirectUrl) {
    redirect(HttpStatus.FOUND.value(), redirectUrl);
  }

  /** {@inheritDoc} */
  @Override
  public void redirect(int code, String redirectUrl) {
    if (alreadyDone.compareAndSet(false, true)) {
      if (HttpStatus.MOVED_PERMANENTLY.value() == code || HttpStatus.FOUND.value() == code) {
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put(HttpHeader.LOCATION, redirectUrl);
        windwardResponse.write(code, MediaType.TEXT_PLAIN_VALUE.value, headerMap, null);
        return;
      }
      throw new WindwardException("Illegal redirecting code" + code);
    } else {
      logger.atWarn().log("Redirecting to " + redirectUrl + " failed, Request already done");
    }
  }

  /** {@inheritDoc} */
  @Override
  public <M> void html(String viewUrl, M model) {
    if (alreadyDone.compareAndSet(false, true)) {
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
              HttpStatus.OK.value(),
              MediaType.TEXT_HTML_VALUE.value,
              Collections.emptyMap(),
              renderedView);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } else {
        throw new WindwardException("Unsupported View extension!");
      }
    } else {
      logger.atWarn().log("Failed to response view, Request already done");
    }
  }
}
