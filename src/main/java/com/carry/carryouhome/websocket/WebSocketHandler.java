package com.carry.carryouhome.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            // 消息接收
            String requestStr = ((TextWebSocketFrame) msg).text();
            System.out.println("我接受到的消息是：" + requestStr);
            // 消息回复
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame("echo");
            ctx.channel().writeAndFlush(textWebSocketFrame);
        } else {
            System.out.println("这是一个非文本消息 不做处理");
        }
    }
}
