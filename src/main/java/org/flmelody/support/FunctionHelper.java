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

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import org.flmelody.core.exception.WindwardException;

/**
 * @author esotericman
 */
public final class FunctionHelper {

  private FunctionHelper() {
    throw new UnsupportedOperationException();
  }

  /**
   * Parse functions generic type, and return function's definition.
   *
   * @param function a function
   * @return generic type map
   */
  public static <F> FunctionDefinition resolveFunction(F function) {
    return findFunctionSmartly(function);
  }

  private static <F> FunctionDefinition findFunctionSmartly(F function) {
    String functionClassName = function.getClass().getName();
    boolean lambda = functionClassName.contains("$$Lambda");
    // Not lambda function
    if (!lambda) {
      return findRegularFunction(function);
    }
    if (function instanceof Serializable) {
      return findLambdaFunction(function);
    }
    return FunctionDefinition.empty();
  }

  // Use to non lambda function
  private static <F> FunctionDefinition findRegularFunction(F function) {
    FunctionDefinition.FunctionDefinitionBuilder builder = FunctionDefinition.builder();
    Method method = function.getClass().getMethods()[0];
    Class<?>[] parameterTypes = method.getParameterTypes();
    builder
        .classname(function.getClass().getName())
        .method(method.getName())
        .parameterType(parameterTypes.length == 0 ? null : parameterTypes[0])
        .returnType(method.getReturnType());
    return builder.build();
  }

  // Lambda function
  private static <F> FunctionDefinition findLambdaFunction(F function) {
    FunctionDefinition.FunctionDefinitionBuilder builder = FunctionDefinition.builder();
    try {
      SerializedLambda serializedLambda = getSerializedLambda(function);
      String instantiatedMethodType = serializedLambda.getInstantiatedMethodType();
      String parameterTypeString =
          instantiatedMethodType
              .substring(2, instantiatedMethodType.indexOf(";"))
              .replace("/", ".");
      String resultTypeString =
          instantiatedMethodType
              .substring(
                  instantiatedMethodType.indexOf(")") + 2, instantiatedMethodType.length() - 1)
              .replace("/", ".");
      Class<?> parameterType =
          Class.forName(parameterTypeString, true, function.getClass().getClassLoader());
      Class<?> resultType =
          Class.forName(resultTypeString, true, function.getClass().getClassLoader());
      builder
          .classname(serializedLambda.getImplClass().replace("/", "."))
          .method(serializedLambda.getImplMethodName())
          .parameterType(parameterType)
          .returnType(resultType);
    } catch (Exception e) {
      throw new WindwardException(e);
    }
    return builder.build();
  }

  private static <F> SerializedLambda getSerializedLambda(F lambda) {
    return writeReplace(lambda);
  }

  private static <F> SerializedLambda writeReplace(F lambda) {
    try {
      Method method = lambda.getClass().getDeclaredMethod("writeReplace");
      method.setAccessible(true);
      return (SerializedLambda) method.invoke(lambda);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Failed to obtain writeReplace method", e);
    }
  }
}
