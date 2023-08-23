package org.maple2.client.core;

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
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.Watcher;
import org.maple.factory.ZookeeperFactory;
import org.maple2.client.constant.Constants;
import org.maple2.client.handler.SimpleClientHandler;
import org.maple2.client.param.ClientRequest;
import org.maple2.client.param.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import static org.maple2.client.core.ChannelManager.i;

public class TCPClient {
//    static Set<String> realPath = new HashSet<String>();
    static final Bootstrap bootstrap = new Bootstrap();
    static ChannelFuture future = null;
    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
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
        String host = "localhost";
        int port = 7000;
        //创建临时节点
        //将服务器注册到zookeeper中
        CuratorFramework client = ZookeeperFactory.create();
        try {
            //获取服务器列表
            List<String> serverPath =  client.getChildren().forPath(Constants.SERVER_PATH);
            //加上zk监听服务器的变化
            CuratorWatcher watcher = new ServerWatcher();
            client.getChildren().usingWatcher(watcher ).forPath(Constants.SERVER_PATH);

            for(String sp : serverPath){
                String[] str = sp.split("#");
                ChannelManager.realPath.add(str[0]+"#"+str[1]);
                ChannelFuture future = TCPClient.bootstrap.connect(str[0], Integer.parseInt(str[1]));

                ChannelManager.add(future);
            }
//            if(realPath.size()>0){
//                String[] hostAndPort = realPath.toArray()[0].toString().split("#");
//                host = hostAndPort[0];
//                port =Integer.parseInt(hostAndPort[1]) ;
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        try {
//            future = bootstrap.connect(host, port).sync();
//
////            InetAddress inetAddress = InetAddress.getLocalHost();
////            future.channel().writeAndFlush( "from client "+inetAddress.getHostAddress()+": Hi!I am client.");
////            future.channel().writeAndFlush("\r\n");
////            future.channel().closeFuture().sync();
//
//
////            future.channel().writeAndFlush("test to server");
////            future.channel().writeAndFlush("\r\n");
//            // 为什么少了这句话，无法收到服务器返回的消息？？？
////            future.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
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
        future = ChannelManager.get(ChannelManager.position);
        future.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
        DefaultFuture df = new DefaultFuture(request);
        return df.get(df.getTimeout());
    }
    public static void main(String[] args) {

    }
}
