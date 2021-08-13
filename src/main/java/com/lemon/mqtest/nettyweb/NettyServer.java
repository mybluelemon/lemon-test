package com.lemon.mqtest.nettyweb;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NettyServer {

    @PostConstruct
    private void init() throws InterruptedException {
        //启动bootstrap
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workgroup = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();
        sb.option(ChannelOption.SO_BACKLOG, 1024);
        sb.group(bossGroup,workgroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(6688)
                .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("收到新连接");
                        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        //以块的方式来写的处理器
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new HttpObjectAggregator(8192));
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
                        ch.pipeline().addLast(new MyWebSocketHandler());

                    }
                });
        ChannelFuture channelFuture = sb.bind().sync();
            channelFuture.channel().closeFuture().sync(); // 关闭服务器通道

    }


}
