package org.maple.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.maple.client.DefaultFuture;
import org.maple.util.Response;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //从服务器端读取数据
        if(msg.toString().equals("ping")){
            //此时产生读写空闲
            //System.out.println("收到读写空闲ping,向服务端发送pong");
            ctx.channel().writeAndFlush("pong\r\n");
            return;
        }
        System.out.println("[客户端接收到读事件，准备接收服务器的返回结果...]");

        //设置response
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        //接收response
        DefaultFuture.receive(response);
        System.out.println("response 接收完毕");
        System.out.println(response);

    }
}
