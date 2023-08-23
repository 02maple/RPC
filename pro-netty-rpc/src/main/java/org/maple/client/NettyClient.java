package org.maple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.maple.handler.SimpleClientHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        //创建启动类
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取管道
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new SimpleClientHandler());
                            pipeline.addLast(new StringEncoder());
//                            pipeline.addLast(new SimpleClientHandler());

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8180).sync();

            InetAddress inetAddress = InetAddress.getLocalHost();
            channelFuture.channel().writeAndFlush( "from client "+inetAddress.getHostAddress()+": Hi!I am client.");
            channelFuture.channel().writeAndFlush("\r\n");
            channelFuture.channel().closeFuture().sync();

//            Object result = channelFuture.channel().attr(AttributeKey.valueOf("mm")).get();
//            System.out.println("服务器返回:"+result);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
