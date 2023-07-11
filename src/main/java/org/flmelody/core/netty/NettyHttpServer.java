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
  public void run() throws Exception {
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

      ChannelFuture f = b.bind(port).sync();
      logger.info("Server started success on port {}", port);
      f.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      logger.info("Server shutdown");
    }
  }
}
