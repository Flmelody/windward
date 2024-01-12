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

package org.flmelody.core.ws.codec;

import io.netty.channel.ChannelHandler;
import org.flmelody.core.Order;

/**
 * Do not use this interface directly; you should always use its subclasses. If you must use it,
 * make sure it conforms to Netty's codec specification.
 *
 * @see WebSocketDecoder
 * @see WebSocketEncoder
 * @author esotericman
 */
public interface WebSocketCodec extends Order, ChannelHandler {}
