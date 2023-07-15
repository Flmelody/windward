package org.flmelody.util;

/**
 * @author esotericman
 */
public final class UrlUtil {
  public static final String SLASH = "/";

  private UrlUtil() {}

  public static String buildUrl(String... path) {
    if (path == null || path.length == 0) {
      throw new IllegalArgumentException();
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < path.length; i++) {
      if (i == 0) {
        if (!path[i].startsWith(SLASH)) {
          path[i] = SLASH + path[i];
        }
        if (!path[i].endsWith(SLASH)) {
          path[i] += SLASH;
        }
      } else if (path[i].startsWith(SLASH)) {
        path[i] = path[i].replaceFirst("/", "");
      }
      stringBuilder.append(path[i]);
    }
    return stringBuilder.toString();
  }
}
