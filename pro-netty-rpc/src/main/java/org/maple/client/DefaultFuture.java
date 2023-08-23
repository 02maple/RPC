package org.maple.client;

import org.maple.util.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {

    public static ConcurrentHashMap<Long,DefaultFuture> allDefaultFuture
            = new ConcurrentHashMap<>();
    private Response response;

    public Response getResponse()    {
        return response;
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
    public Response get(){
        lock.lock();
        try {
            while(!done()) {
                condition.await();
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

}
