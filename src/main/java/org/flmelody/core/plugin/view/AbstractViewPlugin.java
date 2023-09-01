package org.flmelody.core.plugin.view;

import java.util.Map;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public abstract class AbstractViewPlugin implements ViewPlugin {
  protected String templateLocationPrefix;

  // for custom plugin
  public AbstractViewPlugin() {}

  public AbstractViewPlugin(String templateLocationPrefix) {
    this.templateLocationPrefix = templateLocationPrefix;
  }

  public void setTemplateLocationPrefix(String templateLocationPrefix) {
    if (this.templateLocationPrefix != null) {
      // templateLocationPrefix already bound
      // overwrite it ? maybe more flexible
      return;
    }
    this.templateLocationPrefix = templateLocationPrefix;
  }

  public final String resolveView(String viewUrl, Map<String, Object> model) throws Exception {
    return render(UrlUtil.buildUrl(templateLocationPrefix, viewUrl), model);
  }
}
