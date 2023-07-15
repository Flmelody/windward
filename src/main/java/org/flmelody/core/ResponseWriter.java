package org.flmelody.core;

/**
 * @author esotericman
 */
public interface ResponseWriter {
  /**
   * write response data
   *
   * @param code http code
   * @param data data
   * @param <T> data type
   */
  <T> void write(int code, T data);

  /**
   * write response data
   *
   * @param code http code
   * @param contentType contentType
   * @param data data
   * @param <T> data type
   */
  <T> void write(int code, String contentType, T data);

  /**
   * write response data
   *
   * @param code http code
   * @param contentType contentType
   * @param data data
   * @param close close connection
   * @param <T> data type
   */
  <T> void write(int code, String contentType, T data, boolean close);

  /**
   * write response data
   *
   * @param code http code
   * @param contentType contentType
   * @param data data
   * @param <T> data type
   */
  <T> void writeAndClose(int code, String contentType, T data);

  /** close connection */
  void close();
}
