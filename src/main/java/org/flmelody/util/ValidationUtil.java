package org.flmelody.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;
import org.flmelody.core.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public final class ValidationUtil {
  private static final ValidatorFactory validatorFactory;
  private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

  static {
    try {
      validatorFactory = Validation.buildDefaultValidatorFactory();
    } catch (Throwable e) {
      logger.error("Failed to init validator", e);
      throw e;
    }
  }

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
    if (Objects.nonNull(validate) && !validate.isEmpty()) {
      ConstraintViolation<T> constraintViolation = validate.stream().findFirst().get();
      throw new ValidationException(
          constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage());
    }
    return target;
  }
}
