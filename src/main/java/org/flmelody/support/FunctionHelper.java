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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.flmelody.core.exception.WindwardException;

/**
 * @author esotericman
 */
public final class FunctionHelper {

  private FunctionHelper() {
    throw new UnsupportedOperationException();
  }

  /**
   * Parse functions generic type, and return a map that uses functions input type as key and result
   * type as value.
   *
   * @param function a function
   * @return generic type map
   */
  public static <F> Map<Class<?>, Class<?>> resolveFunction(F function) {
    return findFunctionSmartly(function);
  }

  private static <F> Map<Class<?>, Class<?>> findFunctionSmartly(F function) {
    String functionClassName = function.getClass().getName();
    int lambdaMarkerIndex = functionClassName.indexOf("$$Lambda$");
    // Not lambda function
    if (lambdaMarkerIndex == -1) {
      return findRegularFunction(function);
    }
    if (function instanceof Serializable) {
      return findLambdaFunction(function);
    }
    return Collections.emptyMap();
  }

  // Use to non lambda function
  private static <F> Map<Class<?>, Class<?>> findRegularFunction(F function) {
    Map<Class<?>, Class<?>> functionMap = new HashMap<>();
    Method method = function.getClass().getMethods()[0];
    Class<?>[] parameterTypes = method.getParameterTypes();
    functionMap.put(parameterTypes.length == 0 ? null : parameterTypes[0], method.getReturnType());
    return functionMap;
  }

  // Lambda function
  private static <F> Map<Class<?>, Class<?>> findLambdaFunction(F function) {
    Map<Class<?>, Class<?>> functionMap = new HashMap<>();
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
      functionMap.put(parameterType, resultType);
    } catch (Exception e) {
      throw new WindwardException(e);
    }
    return functionMap;
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