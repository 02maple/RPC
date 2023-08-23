package org.maple.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.maple.util.Response;
import org.maple.util.ServerRequest;
import org.maple.medium.Medium;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Executor exec = Executors.newFixedThreadPool(10);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ServerRequest serverRequest = JSONObject.parseObject(msg.toString(), ServerRequest.class);
        //交给中介处理
        //创建中介单例对象
        Medium medium = Medium.newInstance();
        Response response = medium.process(serverRequest);
        /*
        *   此处的setId是问题所在，当客户端等待服务器返回的response的时候，会await，需要服务器的handler
        * 进行一次writeAndFlush，但是此处setId卡住了进程，服务器无法writeAndFlush，导致客户端死等，程序
        * 被阻碍进行。为什么会由于setId卡住？
        *
        *
        * */
//        response.setId(serverRequest.getId());
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"\r\n");
        System.out.println("服务器response发送完毕");
//______________________________________________________________________________

//        exec.execute(new Runnable() {
//
//            @Override
//            public void run() {
//                ServerRequest serverRequest = JSONObject.parseObject(msg.toString(), ServerRequest.class);
//                System.out.println(serverRequest.getCommand());
//                Medium medium = Medium.newInstance();//生成中介者模式
//
//                Response response = medium.process(serverRequest);
//
//                //向客户端发送Resonse
//                ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"\r\n");
//            }
//        });

    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if(evt instanceof IdleStateEvent){
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if(event.state().equals(IdleState.READER_IDLE)){
//                //读空闲产生
//                System.out.println("---读空闲---");
//                ctx.channel().close();
//            }else if (event.state().equals(IdleState.WRITER_IDLE)){
//                //写空闲产生
//                System.out.println("---写空闲---");
//            } else if (event.state().equals(IdleState.ALL_IDLE)) {
//                //读写空闲产生
//                System.out.println("---读写空闲---");
//                ctx.channel().writeAndFlush("ping\r\n");
//            }
//        }
//    }
}
