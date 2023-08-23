package org.maple.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.maple.util.Response;
import org.maple.util.ServerRequest;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //当服务器收到消息时，回送一个 ok
//       ctx.channel().writeAndFlush("ok1 \r\n");
        ServerRequest serverRequest = JSONObject.parseObject(msg.toString(), ServerRequest.class);
        Response response = new Response();
        response.setId(serverRequest.getId());
        response.setResult("is OK");
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"\r\n");



    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state().equals(IdleState.READER_IDLE)){
                //读空闲产生
                System.out.println("---读空闲---");
                ctx.channel().close();
            }else if (event.state().equals(IdleState.WRITER_IDLE)){
                //写空闲产生
                System.out.println("---写空闲---");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                //读写空闲产生
                System.out.println("---读写空闲---");
                ctx.channel().writeAndFlush("ping\r\n");
            }
        }
    }
}
