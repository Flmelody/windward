package org.flmelody.json;

/**
 * base interface for serializing or deserializing json
 *
 * @author esotericman
 */
public interface JsonPlugin {

  /**
   * convert data into json string
   *
   * @param data data
   * @param <I> type of data
   * @return json string
   */
  <I> String toJson(I data);

  /**
   * convert json string into specific class
   *
   * @param json json string
   * @param clazz class
   * @param <O> type of class
   * @return converted object
   */
  <O> O toObject(String json, Class<O> clazz);
}
