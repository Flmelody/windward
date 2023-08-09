/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
