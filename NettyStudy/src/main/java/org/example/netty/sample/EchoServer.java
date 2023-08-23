package org.example.netty.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class EchoServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = msg instanceof ByteBuf ? ((ByteBuf)msg): null;
                                System.out.println(byteBuf.toString(Charset.defaultCharset()));

                                //添加读取逻辑
                                ByteBuf res = ctx.alloc().buffer();
                                res.writeBytes("server:".getBytes());
                                res.writeBytes(byteBuf);

                                ctx.writeAndFlush(res);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
