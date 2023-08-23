package org.example.netty.inoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个出站handler，对数据进行编码
        pipeline.addLast(new MyLongtoByteEncoder());

        //从服务器接收消息
        pipeline.addLast(new MyByteToLongDecoder());

        //加入一个自定义handler
        pipeline.addLast(new MyClientHandler());
    }
}
