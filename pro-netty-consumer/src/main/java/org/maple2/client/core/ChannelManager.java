package org.maple2.client.core;

import io.netty.channel.ChannelFuture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelManager {
    static CopyOnWriteArrayList<String> realPath = new CopyOnWriteArrayList<>();
    static AtomicInteger position = new AtomicInteger(0);
    //负责管理连接
    public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();
    public static void removeChannel(ChannelFuture cf){
        channelFutures.remove(cf);
    }
    public static void add(ChannelFuture cf){
        channelFutures.add(cf);
    }
    public static void clear(){
        channelFutures.clear();
    }
    public static ChannelFuture get(AtomicInteger i) {
        int size = channelFutures.size();
        ChannelFuture cf = null;
        if(i.get()>size){
            cf=channelFutures.get(0);
            ChannelManager.position=new AtomicInteger(1);
        }else {
            cf=channelFutures.get(i.getAndIncrement());
//            ChannelManager.position.incrementAndGet();
        }
        return cf;
    }
}
