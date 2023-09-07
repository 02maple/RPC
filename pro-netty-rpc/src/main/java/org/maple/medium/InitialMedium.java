package org.maple.medium;

import org.maple.annotation.Remote;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class InitialMedium implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //初始化之前，直接返回bean
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //判断bean中的类是否有Controller注解，如果有，则获取该类中的的method
        if(bean.getClass().isAnnotationPresent(Remote.class)){
//        if(bean.getClass().isAnnotationPresent(Controller.class)){
            //存储类中的方法
            Method[] methods = bean.getClass().getDeclaredMethods();
//            Map<String, BeanMethod> beanMethodMap = Media.beanMethodMap;
            for(Method m:methods){
                //作为key传入
                //类包名加上方法名，全路径
//                String beanMethodKey=bean.getClass().getInterfaces()[0].getName()+"."+m.getName();
                String beanMethodKey=bean.getClass().getName()+"."+m.getName();
                //修改 2023年8月24日15点56分
//                String beanMethodKey=bean.getClass(). getSuperclass() .getName()+"."+m.getName();

                Map<String, BeanMethod> beanMethodMap = Media.beanMethodMap;

                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setM(m);

                beanMethodMap.put(beanMethodKey,beanMethod);
//                System.out.println(beanMethodMap.get("org.maple.users.remote.UserRemoteImpl.saveUsers"));
            }

//            System.out.println(Media.beanMethodMap.get("org.maple.users.remote.UserRemoteImpl.saveUsers"));
//            System.out.println(Media.beanMethodMap.get("org.maple.users.remote.UserRemoteImpl.saveUser"));
//            System.out.println(Media.beanMethodMap.hashCode());
        }
        return bean;
    }
}
