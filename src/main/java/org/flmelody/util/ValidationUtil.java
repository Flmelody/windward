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

package org.flmelody.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.flmelody.core.exception.ValidationException;
import org.flmelody.core.plugin.json.JsonPlugin;

/**
 * @author esotericman
 */
public final class ValidationUtil {
  private static final ValidatorFactory validatorFactory =
      Validation.buildDefaultValidatorFactory();

  private ValidationUtil() {}

  /**
   * Try to convert json content to target object
   *
   * @param jsonPlugin jsonPlugin
   * @param content json
   * @param targetClass target class that convert to
   * @param groups validator group
   * @param <T> return type
   * @return object that convert from json content
   */
  public static <T> T validate(
      JsonPlugin jsonPlugin, String content, Class<T> targetClass, Class<?>... groups)
      throws ValidationException {
    return validate(jsonPlugin.toObject(content, targetClass), groups);
  }

  /**
   * Try to convert json content to target object
   *
   * @param jsonPlugin jsonPlugin
   * @param content json
   * @param type type of target class
   * @param groups validator group
   * @param <T> return type
   * @return object that convert from json content
   */
  public static <T> T validate(JsonPlugin jsonPlugin, String content, Type type, Class<?>... groups)
      throws ValidationException {
    return validate(jsonPlugin.toObject(content, type), groups);
  }

  private static <T> T validate(T target, Class<?>... groups) {
    Validator validator = validatorFactory.getValidator();
    Set<ConstraintViolation<T>> validate;
    if (Objects.isNull(groups)) {
      validate = validator.validate(target);
    } else {
      validate = validator.validate(target, groups);
    }
    Optional<ConstraintViolation<T>> first = validate.stream().findFirst();
    if (first.isPresent()) {
      ConstraintViolation<T> constraintViolation = first.get();
      throw new ValidationException(
          constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage());
    }
    return target;
  }
}
