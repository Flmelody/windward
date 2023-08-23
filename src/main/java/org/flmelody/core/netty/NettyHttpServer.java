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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import org.flmelody.core.HttpServer;
import org.flmelody.core.exception.ServerException;
import org.flmelody.core.netty.handler.HttpServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class NettyHttpServer implements HttpServer {

  private final int port;
  static Logger logger = LoggerFactory.getLogger(NettyHttpServer.class);

  public NettyHttpServer(int port) {
    this.port = port;
  }

  @Override
  public void run() throws ServerException {
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline p = ch.pipeline();
                  p.addLast(new HttpServerCodec());
                  p.addLast(
                      new CorsHandler(
                          CorsConfigBuilder.forAnyOrigin()
                              .allowNullOrigin()
                              .allowCredentials()
                              .build()));
                  p.addLast(new HttpObjectAggregator(65536));
                  p.addLast(new HttpServerHandler());
                }
              });
      try {
        ChannelFuture f = b.bind(port).sync();
        logger.info("Server started success on port {}", port);
        f.channel().closeFuture().sync();
      } catch (InterruptedException e) {
        logger.info("Server run error", e);
        Thread.currentThread().interrupt();
        throw new ServerException("Server run error");
      }
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      logger.info("Server shutdown");
    }
  }
}
