package org.flmelody.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flmelody.core.exception.JsonDeserializeException;
import org.flmelody.core.exception.JsonSerializeException;

/**
 * @author esotericman
 */
public class JacksonUtil {
  static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private JacksonUtil() {}

  /**
   * convert data into json string
   *
   * @param data data
   * @param <I> type of data
   * @return json string
   */
  public static <I> String toJson(I data) {
    try {
      return objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new JsonSerializeException(e);
    }
  }

  /**
   * convert json string into specific class
   *
   * @param json json string
   * @param clazz class
   * @param <O> type of class
   * @return converted object
   */
  public static <O> O toObject(String json, Class<O> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new JsonDeserializeException(e);
    }
  }
}
