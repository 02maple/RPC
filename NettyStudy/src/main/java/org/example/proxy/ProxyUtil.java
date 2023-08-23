package org.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyUtil {
    public static Star creatProxy(BigStar star){
        /*
        public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
                                     参数1：指定一个类加载器
                                     参数2：指定生成的代理长什么样子，也就是有哪些方法
                                     参数3：用来指定生成的对象要干什么事情

         */
        Star starProxy = (Star) Proxy.newProxyInstance(ProxyUtil.class.getClassLoader(),
                new Class[]{Star.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //代理对象要做的事情
                        if(method.getName().equals("sing")){
                            System.out.println("准备话筒，收钱10万");
                        } else if (method.getName().equals("dance")) {
                            System.out.println("准备舞台，收钱20万");
                        }
                        //在这里调用了传入的star中对应method.getName()的方法，并返回
                        return method.invoke(star,args);

                    }
                });
        return starProxy;
    }
}
