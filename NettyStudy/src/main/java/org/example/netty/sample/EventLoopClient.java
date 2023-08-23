package org.example.netty.sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Scanner;


public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));

        Channel channel = channelFuture.sync().channel();
        //log.debug("{}",channel);
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (true){
                String line = scanner.nextLine();
                if("q".equals(line)){
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        },"Input").start();

        ChannelFuture channelFuture1 = channel.closeFuture();
       // log.debug("waiting close...");
        channelFuture1.sync();
        System.out.println("处理关闭后的操作");
        System.out.println("开始完美退出");
        group.shutdownGracefully();
        System.out.println("退出成功");
    }
}
