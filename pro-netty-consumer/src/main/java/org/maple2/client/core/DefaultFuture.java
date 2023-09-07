package org.maple2.client.core;

import org.maple2.client.param.ClientRequest;
import org.maple2.client.param.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {

    public static ConcurrentHashMap<Long,DefaultFuture> allDefaultFuture
            = new ConcurrentHashMap<>();
    private Response response;

    public Response getResponse(){
        return response;
    }
    private long timeout = 2*60*1000;
    private final long starttime = System.currentTimeMillis();
    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStarttime() {
        return starttime;
    }


    public void setResponse(Response response) {
        this.response = response;
    }

    //创建可重入锁
    final ReentrantLock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();
    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(),this);
    }

    //主线程获取数据，需要等待结果
    public Response get(long time){
        lock.lock();
        try {
            while(!done()) {
                condition.await(time, TimeUnit.MILLISECONDS);
                if((System.currentTimeMillis()-starttime)>time){
                    System.out.println("请求超时!");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return this.response;
    }

    private boolean done() {
        if(this.response!=null){
            return true;
        }
        return false;
    }

    public static void receive(Response response){

        if(response != null){
            DefaultFuture future = allDefaultFuture.get(response.getId());
            if(future != null){
                Lock lock = future.lock;
                lock.lock();
                try {
                    future.setResponse(response);
                    future.condition.signal();
                    allDefaultFuture.remove(future);//别忘记remove
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    static class FutureThread extends Thread{
        @Override
        public void run() {
            Set<Long> ids =  allDefaultFuture.keySet();
            for(Long id :ids){
                DefaultFuture df = allDefaultFuture.get(id);
                if(df == null){
                    allDefaultFuture.remove(df);
                }else {
                    //假如链路超时
                    if(df.getTimeout()<System.currentTimeMillis()-df.getStarttime()){
                        Response response = new Response();
                        response.setId(id);
                        response.setStatus("333333");
                        response.setMassage("请求超时");
                        receive(response);
                    }
                }
            }
        }
    }

    static {
        FutureThread futureThread = new FutureThread();
        futureThread.setDaemon(true);
        futureThread.start();
    }


}
