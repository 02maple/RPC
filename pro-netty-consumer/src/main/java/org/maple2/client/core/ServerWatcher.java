package org.maple2.client.core;

import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.maple.factory.ZookeeperFactory;

import java.util.HashSet;
import java.util.List;

public class ServerWatcher implements CuratorWatcher {
    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        CuratorFramework client = ZookeeperFactory.create();
        String path = watchedEvent.getPath();

        client.getChildren().usingWatcher(this);
        List<String> serverPaths=client.getChildren().forPath(path);
        ChannelManager.realPath.clear();

//        TCPClient.realPath.clear();
//        TCPClient.realPath = new HashSet<String>();

        for(String sp : serverPaths){
            String[] str =  sp.split("#");
            ChannelManager.realPath.add(str[0]+"#"+str[1]);
        }
        ChannelManager.clear();
        for (String realServer :ChannelManager.realPath){
            String[] str =  realServer.split("#");
            ChannelFuture future = TCPClient.bootstrap.connect(str[0], Integer.parseInt(str[1]));
            ChannelManager.add(future);
        }


    }
}
