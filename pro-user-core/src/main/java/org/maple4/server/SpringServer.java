package org.maple4.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


//@Configuration
//@ComponentScan("org.maple")
//public class SpringServer {
//    public static void main(String[] args) throws InterruptedException {
//        ApplicationContext context =
//                new AnnotationConfigApplicationContext(SpringServer.class);
////        context.wait();
//    }
//}


@Configuration
@ComponentScan("org.maple")
public class SpringServer {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringServer.class);

    }
}