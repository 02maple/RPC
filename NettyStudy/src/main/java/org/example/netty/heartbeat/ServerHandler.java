package org.example.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleState = (IdleStateEvent) evt;
            String msg = null;
            switch (idleState.state()){
                case READER_IDLE -> msg="读空闲";
                case WRITER_IDLE -> msg="写空闲";
                case ALL_IDLE -> msg="读写空闲";
            }
            System.out.println(ctx.channel().remoteAddress()+"--超时事件--"+msg);
        }
    }

}
