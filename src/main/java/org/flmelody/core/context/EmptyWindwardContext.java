package org.flmelody.core.context;

import org.flmelody.core.WindwardRequest;

import java.util.List;

/**
 * @author esotericman
 */
public class EmptyWindwardContext implements WindwardContext {
  @Override
  public <P> P getRequestParameter(String parameterName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getRequestParameters(String parameterName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getRequestBody() {
    throw new UnsupportedOperationException();
  }

  @Override
  public WindwardRequest windwardRequest() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Boolean isClosed() {
    return Boolean.TRUE;
  }

  @Override
  public <I> I readJson(Class<I> clazz) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <I> I bindJson(Class<I> clazz, Class<?>... groups) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> void writeJson(int code, T data) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeString(int code, String data) {
    throw new UnsupportedOperationException();
  }
}
