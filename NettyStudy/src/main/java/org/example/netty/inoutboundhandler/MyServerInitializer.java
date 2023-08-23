package org.example.netty.inoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入相关的入站handler，解码 MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());

        //从服务器返回消息给客户端，需要编码器，加入该编码器
        pipeline.addLast(new MyLongtoByteEncoder());

        //自定义handler处理业务逻辑
        pipeline.addLast(new MyServerHandler());
    }
}
