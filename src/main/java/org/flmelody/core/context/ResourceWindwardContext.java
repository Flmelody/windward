package org.flmelody.core.context;

import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;

/**
 * @author esotericman
 */
public class ResourceWindwardContext extends AbstractHttpWindwardContext {
  private final String matchedPath;

  public ResourceWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse, String matchedPath) {
    super(windwardRequest, windwardResponse);
    this.matchedPath = matchedPath;
  }

  public String getMatchedPath() {
    return matchedPath;
  }
}
