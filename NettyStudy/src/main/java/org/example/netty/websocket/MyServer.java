package org.example.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.netty.heartbeat.ServerHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) throws Exception{
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            //基于http协议，需要使用http的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，需要使用ChunkedWrite处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            1.http的数据在传输过程中是分段的，HttpObjectAggregator就是可以将多个段聚合
                            2.这就是浏览器发送大量数据时，会发出多次http请求的原因
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            1.对于websocket时以帧（frame）的形式传递
                            2.可以看到webSocketFrame 下面有六个子类
                            3.浏览器请求时ws://localhost:8080/hello 表示请求的uri
                            4.WebSocketServerProtocolHandler 的核心功能：把http协议升级为ws协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //自定义的handler，处理业务逻辑
                            pipeline.addLast(new MyServerHandler());
                        }

                    });

            //启动服务器
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();

        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}





