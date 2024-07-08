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

package org.flmelody.core.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.ssl.OptionalSslHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.util.Objects;
import org.flmelody.core.HttpServer;
import org.flmelody.core.SslPair;
import org.flmelody.core.exception.ServerException;
import org.flmelody.core.netty.handler.HttpEventHandler;
import org.flmelody.core.netty.handler.HttpServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class NettyHttpServer implements HttpServer {
  private static final Logger logger = LoggerFactory.getLogger(NettyHttpServer.class);
  private final int port;

  public NettyHttpServer(int port) {
    this.port = port;
  }

  @Override
  public void run(Object... args) throws ServerException {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ServerChannelInitializer(args));
      try {
        ChannelFuture f = b.bind(port).sync();
        logger.atInfo().log("Service started successfully, listening on port {}", port);
        f.channel().closeFuture().sync();
      } catch (InterruptedException e) {
        logger.atInfo().log("Service run error", e);
        Thread.currentThread().interrupt();
        throw new ServerException("Service run error");
      }
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      logger.atInfo().log("Server shutdown");
    }
  }

  /** Initializer for server */
  private static class ServerChannelInitializer extends ChannelInitializer<Channel> {
    private SslContext sslContext;
    private boolean forceSsl;

    private ServerChannelInitializer(Object... args) {
      detectSsl(args);
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
      ChannelPipeline p = ch.pipeline();
      if (Objects.nonNull(sslContext)) {
        if (forceSsl) {
          p.addLast(sslContext.newHandler(ch.alloc()));
        } else {
          p.addLast(new OptionalSslHandler(sslContext));
        }
      }
      p.addLast(new HttpServerCodec());
      p.addLast(
          new CorsHandler(
              CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build()));
      p.addLast(new HttpObjectAggregator(65536));
      p.addLast(new ChunkedWriteHandler());
      p.addLast(new HttpServerHandler());
      p.addLast(new HttpEventHandler());
    }

    private void detectSsl(Object... args) {
      if (args != null) {
        for (Object arg : args) {
          if (Objects.isNull(sslContext)) {
            if (arg instanceof SslPair) {
              SslPair sslPair = (SslPair) arg;
              try {
                this.forceSsl = sslPair.forceStatus();
                this.sslContext =
                    SslContextBuilder.forServer(sslPair.certFile(), sslPair.keyFile()).build();
              } catch (Exception e) {
                throw new ServerException("Failed to initialize ssl context");
              }
            }
          }
        }
      }
    }
  }
}
