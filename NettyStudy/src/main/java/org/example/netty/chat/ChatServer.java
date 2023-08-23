package org.example.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServer {
    private final int PORT;

    public ChatServer(int p){
        this.PORT = p;
    }

    public void run() throws InterruptedException {
        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {


            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入一个解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //向pipeline加入一个编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自己的handler业务处理
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            System.out.println("netty server start...");
            ChannelFuture future = serverBootstrap.bind(8880).sync();

            //监听关闭事件
            future.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new ChatServer(8880).run();
    }
}
