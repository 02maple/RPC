package org.example.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerHandler extends SimpleChannelInboundHandler {

    //定义一个channel组 管理所有的channel
    //GlobalEventExecutor.INSTANCE 全局的事件执行器，单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat time = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //handlerAdded 表示一旦连接，第一个被执行
    //将当前channel加入channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端，也就是推送到其他channel
                    /*该方法会将channelGroup中所以的channel遍历并发送方法中的消息*/
        channelGroup.writeAndFlush(time.format(new Date())+" [客户端]"+channel.remoteAddress()+" 加入聊天 (^o^)");
        channelGroup.add(channel);
    }


    //表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线 φ(゜▽゜*)♪");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线 (T_T)");
    }
    //断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"退出聊天 (T_T)");
    }

    //读取数据并且转发
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取当前channel
        Channel channel = ctx.channel();
        //遍历channelGroup，根据不同的情况回送不同的消息
        channelGroup.forEach(channel1 -> {
            if(channel!=channel1){
                //不是当前的channel，转发消息
                channel1.writeAndFlush("[客户端]"+channel.remoteAddress()+": "+(String)msg);
            }else {
                channel1.writeAndFlush("[本机]:"+(String)msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        //关闭通道
        ctx.close();
    }
}
