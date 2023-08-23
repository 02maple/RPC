package org.example.netty.inoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    // decode方法会根据接收的数据重复执行，直到确定没有新的元素被添加到List
    // 如果List不为空，会将List数据传递给下一个handler
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder decode 被调用");
        // long 8个字节
        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
