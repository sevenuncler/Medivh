
package com.carry.carryouhome.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author 李家民
 */
public class CarryWebSocket {
    public void ServerStart() throws InterruptedException {
        // 连接请求处理
        EventLoopGroup bossGroup = new NioEventLoopGroup(3);
        // IO请求处理
        EventLoopGroup workerGroup = new NioEventLoopGroup(5);
        // 基本配置
        ServerBootstrap serverBootstrap =
                new ServerBootstrap().group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
        // 处理器管道配置
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                System.out.println("此时客户端进来了：" + "客户 SocketChannel hashcode=" + ch.hashCode());
                ChannelPipeline pipeline = ch.pipeline();
                // 请求/响应的编解码器
                pipeline.addLast(new HttpServerCodec());

                // 将多消息转换为单一请求/响应对象,解码成FullHttpRequest
                // maxContentLength – 聚合内容的最大长度（以字节为单位）
                pipeline.addLast(new HttpObjectAggregator(65535));

                // WebSocket协议处理
                pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                // 下面才是自定义的WebSocket业务处理器
                pipeline.addLast(new WebSocketHandler());
            }
        });
        // 服务器端口配置及监听
        ChannelFuture channelFuture = serverBootstrap.bind(16668).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("监听端口 16668 成功");
                } else {
                    System.out.println("监听端口 16668 失败");
                }
            }
        });
        // 关闭通道并监听
        channelFuture.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
