package org.maple.client;

import com.alibaba.fastjson.JSONObject;
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
import org.maple.util.Response;

public class TCPClient {

//    static final Bootstrap bootstrap = new Bootstrap();
    static ChannelFuture future = null;
    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
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
//                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new SimpleClientHandler());
                        pipeline.addLast(new StringEncoder());

                    }
                });




            future = bootstrap.connect("localhost", 7077).sync();

//            InetAddress inetAddress = InetAddress.getLocalHost();
//            future.channel().writeAndFlush( "from client "+inetAddress.getHostAddress()+": Hi!I am client.");
//            future.channel().writeAndFlush("\r\n");
//            future.channel().closeFuture().sync();


//            future.channel().writeAndFlush("test to server");
//            future.channel().writeAndFlush("\r\n");
            // 为什么少了这句话，无法收到服务器返回的消息？？？
//            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        finally {
//            group.shutdownGracefully();
//        }

    }

    //1.每个请求都是同一个连接，并发问题
    /******
      Request               Response
      1.唯一请求ID           1.响应唯一识别码ID
      2.请求内容             2.响应结果
     ******/
    //发送数据
    public static Response send(ClientRequest request){
        future.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
        DefaultFuture df = new DefaultFuture(request);
        return df.get();
    }
    public static void main(String[] args) {

    }
}
