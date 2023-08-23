package org.maple2.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


//元注解，表示只能在方法(method)和类(type)上面
@Target({ElementType.TYPE,ElementType.METHOD})
//表示注解在什么地方有效     自定义注解都是runtime时有效
//runtime > class > source
@Retention(RetentionPolicy.RUNTIME)
//表示是否将我们的注解生成的JAVADoc中
@Documented
@Component
public @interface Remote {
    //注解的参数:参数类型+参数名+()
    String value() default "";
}
