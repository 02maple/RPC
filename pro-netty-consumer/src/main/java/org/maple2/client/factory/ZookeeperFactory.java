package org.maple2.client.factory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperFactory {

    //使用Curator
    public static CuratorFramework client;
    public static CuratorFramework create(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        return client;
    }
    public static void main(String[] args) throws Exception {
        //Test
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client  = create();
        client.create().forPath("/netty111");
    }
}
