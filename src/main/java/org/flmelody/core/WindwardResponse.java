package org.flmelody.core;

/**
 * @author esotericman
 */
public class WindwardResponse {
  private Boolean keepConnection;
  private ResponseWriter responseWriter;

  /**
   * write data into response
   *
   * @param code http code
   * @param data data
   * @param <T> data type
   */
  public <T> void write(int code, T data) {
    responseWriter.write(code, data);
    if (!keepConnection) {
      responseWriter.close();
    }
  }

  /**
   * write data into response
   *
   * @param code http code
   * @param contentType response contentType
   * @param data data
   * @param <T> data type
   */
  public <T> void write(int code, String contentType, T data) {
    responseWriter.write(code, contentType, data);
  }

  public static WindwardResponseBuild newBuilder() {
    return new WindwardResponseBuild(new WindwardResponse());
  }

  private WindwardResponse() {}

  /** builder for WindwardResponse */
  public static class WindwardResponseBuild {
    private final WindwardResponse windwardResponse;

    private WindwardResponseBuild(WindwardResponse windwardResponse) {
      this.windwardResponse = windwardResponse;
    }

    public WindwardResponseBuild keepConnection(Boolean keepConnection) {
      this.windwardResponse.keepConnection = keepConnection;
      return this;
    }

    public WindwardResponseBuild responseWriter(ResponseWriter responseWriter) {
      this.windwardResponse.responseWriter = responseWriter;
      return this;
    }

    public WindwardResponse build() {
      return this.windwardResponse;
    }
  }
}
