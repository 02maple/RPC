package org.maple.init;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.maple.constant.Constants;
import org.maple.factory.ZookeeperFactory;
import org.maple.handler.ServerHandler;
import org.maple.handler.SimpleServerHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Component
public class NettyServerInitial implements ApplicationListener<ContextRefreshedEvent> {
    public void start() throws InterruptedException {
        //设置两个线程组，一个接收accept事件，另一个接收channel内事件，处理业务
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup);
            //允许128个通道排队
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    //心跳包，默认为false，这里设置为true
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.channel(NioServerSocketChannel.class) //绑定通道类型
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取管道
                            ChannelPipeline pipeline = ch.pipeline();
                            //添加解码器
                            pipeline.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                            pipeline.addLast(new StringDecoder());
                            //加入长连接心跳
                            pipeline.addLast(new IdleStateHandler(60,
                                    45,20, TimeUnit.SECONDS));
//                            pipeline.addLast(new SimpleServerHandler());
                            pipeline.addLast(new ServerHandler());
                            //添加字符串编码器
                            pipeline.addLast(new StringEncoder());
                        }
                    });

            int port = 7077;
            ChannelFuture channelFuture = serverBootstrap.bind(7077).sync();
            System.out.println("Server Start...");

            //创建临时节点
        //将服务器注册到zookeeper中
             CuratorFramework client = ZookeeperFactory.create();
            //获取地址
            InetAddress inetAddress = InetAddress.getLocalHost();
            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(Constants.SERVER_PATH+"/"+inetAddress.getHostAddress()+"#"+port+"#");
            System.out.println(Constants.SERVER_PATH+"/"+inetAddress.getHostAddress()+"#"+port+"#"+
                                                                    ": Add in Zookeeper [success]");

            channelFuture.channel().closeFuture().sync();
            System.out.println("Server done...");

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            this.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
