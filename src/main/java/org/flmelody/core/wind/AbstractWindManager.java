/*
 * Copyright (C) 2023 Flmelody.
 *
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

package org.flmelody.core.wind;

import java.util.ArrayList;
import java.util.List;
import org.flmelody.core.Windward;
import org.flmelody.core.wind.event.Event;
import org.flmelody.core.wind.listener.Listener;

/**
 * @author esotericman
 */
public abstract class AbstractWindManager implements WindManager {
  private final Windward windward;
  protected final List<Listener> listeners = new ArrayList<>();
  protected final List<Event> events = new ArrayList<>();

  protected AbstractWindManager(Windward windward) {
    this.windward = windward;
  }

  @Override
  public void join(Listener listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  @Override
  public void trigger(Event event) {
    events.add(event);
    for (Listener listener : listeners) {
      listener.cope(windward, event);
    }
  }
}
