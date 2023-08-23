package org.maple2.client.proxy;

import org.maple.client.ClientRequest;
import org.maple.client.TCPClient;
import org.maple2.client.annotation.RemoteInvoke;
import org.maple.util.Response;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokeProxy implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //初始化之前,进行动态代理
        //获取该类中的所有属性
        Field[] fields = bean.getClass().getDeclaredFields();
        //遍历属性集合
        for (Field f:fields) {
            if(f.isAnnotationPresent(RemoteInvoke.class)){
                //判断属性上有没有该注解，如果有的话，进行处理
                //允许修改被该注解标识的属性
                f.setAccessible(true);
                //
                final Map<Method,Class> methodClassMap = new HashMap<Method,Class>();
                //将属性的所所有方法和类型放入到methodClassMap中
                putMethodClass(methodClassMap,f);
                //spring  动态代理
                Enhancer enhancer = new Enhancer();
                //设置属性，获取接口的对象，对哪些接口进行动态代理
                enhancer.setInterfaces(new Class[]{f.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    //当执行到这些方法的时候进行拦截
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        //采用netty客户端去调用服务器
                        ClientRequest request = new ClientRequest();
                        request.setContent(args[0]);

//                        request.setCommand(methodClassMap.get(method).getName()+"."+method.getName());
//                        Response response = TCPClient.send(request);

// --------------------------
                        String command = method.getName();//修改
//						System.out.println("InvokeProxy中的Command是:"+command);
                        request.setCommand(command);

                        Response response = TCPClient.send(request);
                        //----------------------------------------------------------

                        return response;
                    }
                });

                try {
                    f.set(bean,enhancer.create());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    //对属性的所有方法和属性接口类型放入到一个map中
    private void putMethodClass(Map<Method, Class> methodClassMap, Field f) {
        Method[] methods = f.getType().getDeclaredMethods();
        for (Method m:methods){
            methodClassMap.put(m,f.getType());
        }
    }



    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
