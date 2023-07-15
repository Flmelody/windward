package org.flmelody.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.flmelody.core.exception.ValidationException;

/**
 * @author esotericman
 */
public final class ValidationUtil {
  private static final ValidatorFactory validatorFactory =
      Validation.buildDefaultValidatorFactory();

  /**
   * try to convert json content to target object
   *
   * @param content json
   * @param targetClass target class that convert to
   * @param groups validator group
   * @param <T> return type
   * @return object that convert from json content
   */
  public static <T> T validate(String content, Class<T> targetClass, Class<?>... groups)
      throws ValidationException {
    Validator validator = validatorFactory.getValidator();
    T target = JacksonUtil.toObject(content, targetClass);
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
