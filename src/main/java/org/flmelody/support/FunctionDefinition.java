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

package org.flmelody.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author esotericman
 */
public class FunctionDefinition {
  private static final FunctionDefinition EMPTY_DEFINITION = new FunctionDefinition();
  private String classname;
  private String method;
  private final List<Class<?>> parameterTypes = new ArrayList<>();
  private Class<?> returnType;

  private FunctionDefinition() {
  }

  public static FunctionDefinition empty() {
    return EMPTY_DEFINITION;
  }

  public static FunctionDefinitionBuilder builder() {
    return new FunctionDefinitionBuilder(new FunctionDefinition());
  }

  public String getClassname() {
    return classname;
  }

  public String getMethod() {
    return method;
  }

  public List<Class<?>> getParameterTypes() {
    return Collections.unmodifiableList(parameterTypes);
  }

  public Class<?> getReturnType() {
    return returnType;
  }

  /** Builder for FunctionDefinition */
  public static class FunctionDefinitionBuilder {
    private final FunctionDefinition functionDefinition;

    private FunctionDefinitionBuilder(FunctionDefinition functionDefinition) {
      this.functionDefinition = functionDefinition;
    }

    public FunctionDefinitionBuilder classname(String classname) {
      this.functionDefinition.classname = classname;
      return this;
    }

    public FunctionDefinitionBuilder method(String method) {
      this.functionDefinition.method = method;
      return this;
    }

    public FunctionDefinitionBuilder parameterType(Class<?> parameterType) {
      this.functionDefinition.parameterTypes.add(parameterType);
      return this;
    }

    public FunctionDefinitionBuilder returnType(Class<?> returnType) {
      this.functionDefinition.returnType = returnType;
      return this;
    }

    public FunctionDefinition build() {
      return this.functionDefinition;
    }
  }
}
