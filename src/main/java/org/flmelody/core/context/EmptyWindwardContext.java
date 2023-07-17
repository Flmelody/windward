package org.flmelody.core.context;

import org.flmelody.core.WindwardRequest;

import java.util.List;

/**
 * @author esotericman
 */
public class EmptyWindwardContext implements WindwardContext {
  @Override
  public <P> P getRequestParameter(String parameterName) {
    return null;
  }

  @Override
  public List<String> getRequestParameters(String parameterName) {
    return null;
  }

  @Override
  public String getRequestBody() {
    return null;
  }

  @Override
  public WindwardRequest windwardRequest() {
    return null;
  }

  @Override
  public void close() {}

  @Override
  public Boolean isClosed() {
    return null;
  }

  @Override
  public <I> I readJson(Class<I> clazz) {
    return null;
  }

  @Override
  public <I> I bindJson(Class<I> clazz, Class<?>... groups) {
    return null;
  }

  @Override
  public <T> void writeJson(int code, T data) {}

  @Override
  public void writeString(int code, String data) {}
}
