package org.flmelody.core.plugin;

import org.flmelody.core.plugin.json.JsonPlugin;
import org.flmelody.core.plugin.resource.BaseStaticResourcePlugin;
import org.flmelody.core.plugin.view.freemarker.FreemarkerView;
import org.flmelody.core.plugin.view.groovy.GroovyView;
import org.flmelody.core.plugin.view.thymeleaf.ThymeleafView;
import org.flmelody.core.plugin.ws.ExtensionalWebSocketPlugin;

/**
 * Predefine plugin slot.
 *
 * @author esotericman
 */
public enum PluginSlot {
  ANY(Plugin.class),
  /** Slot for json. */
  JSON(JsonPlugin.class),
  /** Slot for groovy template. */
  GROOVY_VIEW(GroovyView.class),
  /** Slot for thymeleaf template. */
  THYMELEAF_VIEW(ThymeleafView.class),
  /** Slot for freemarker template. */
  FREEMARKER_VIEW(FreemarkerView.class),
  /** Slot for websocket. */
  WEBSOCKET(ExtensionalWebSocketPlugin.class),
  /** Slot for static resource. */
  RESOURCE(BaseStaticResourcePlugin.class);

  public final Class<? extends Plugin> clazz;

  PluginSlot(Class<? extends Plugin> clazz) {
    this.clazz = clazz;
  }

  public static PluginSlot slot(Class<? extends Plugin> clazz) {
    for (PluginSlot pluginSlot : PluginSlot.values()) {
      if (pluginSlot.clazz.equals(clazz)) {
        return pluginSlot;
      }
    }
    return ANY;
  }
}
