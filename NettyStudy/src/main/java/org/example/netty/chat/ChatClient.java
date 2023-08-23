package org.example.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class ChatClient {

    private final String host;
    private final int PORT;

    public ChatClient(String host,int port){
        this.host=host;
        this.PORT=port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //得到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入handler
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast("decode", new StringDecoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, PORT).sync();

            Channel channel = channelFuture.channel();
            System.out.println("------"+channel.localAddress()+"------");

            //客户端需要输入信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                //通过channel发送
                channel.writeAndFlush(msg+"\r\n");
            }

        }finally {
            group.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws InterruptedException {
        ChatClient chatClient = new ChatClient("127.0.0.1", 8880);
        chatClient.run();
    }
}
