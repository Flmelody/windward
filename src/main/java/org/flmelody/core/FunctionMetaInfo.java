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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.flmelody.core.context.WindwardContext;
import org.flmelody.core.exception.WindwardException;
import org.flmelody.support.EnhancedFunction;
import org.flmelody.support.FunctionDefinition;
import org.flmelody.support.FunctionHelper;

/**
 * @author esotericman
 */
public class FunctionMetaInfo<I> {
  private final String path;
  private final I function;
  private final Class<? extends WindwardContext> context;
  private final Class<? extends WindwardContext> parameterType;
  private final Class<?> resultType;
  private final Map<String, Object> pathVariables;
  private final FunctionDefinition functionDefinition;
  private final List<?> args;

  public FunctionMetaInfo(
      String path,
      I function,
      Class<? extends WindwardContext> context,
      Map<String, Object> pathVariables,
      Object... args) {
    this.path = path;
    this.function = function;
    this.context = context;
    this.pathVariables = pathVariables;
    // Only for EnhancedFunction
    if (function instanceof EnhancedFunction) {
      EnhancedFunction<?, ?> enhancedFunction = (EnhancedFunction<?, ?>) function;
      FunctionDefinition functionDefinition = FunctionHelper.resolveFunction(enhancedFunction);
      if (functionDefinition.getParameterTypes().isEmpty()) {
        throw new WindwardException("Illegal function!");
      }
      // windward context
      //noinspection unchecked
      this.parameterType =
          (Class<? extends WindwardContext>)
              functionDefinition.getParameterTypes().stream()
                  .findFirst()
                  .orElseThrow(() -> new WindwardException("Missed parameter type!"));
      // all java type
      this.resultType = functionDefinition.getReturnType();
      this.functionDefinition = functionDefinition;
    } else {
      this.functionDefinition = FunctionDefinition.empty();
      this.parameterType = null;
      this.resultType = null;
    }
    if (args != null) {
      this.args = Arrays.asList(args);
    } else {
      this.args = Collections.emptyList();
    }
  }

  public String getPath() {
    return path;
  }

  public I getFunction() {
    return function;
  }

  public Class<? extends WindwardContext> getContext() {
    return context;
  }

  public Class<? extends WindwardContext> getParameterType() {
    return parameterType;
  }

  public Class<?> getResultType() {
    return resultType;
  }

  public Map<String, Object> getPathVariables() {
    return pathVariables;
  }

  public FunctionDefinition getFunctionDefinition() {
    return functionDefinition;
  }

  public List<?> getArgs() {
    return args;
  }
}
