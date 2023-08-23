package org.maple5.controller;

import org.maple.annotation.RemoteInvoke;
import org.maple5.service.BasicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.maple")
@ComponentScan("org.maple2")
@ComponentScan("org.maple3")
public class BasicController {
        public static void main(String[] args) {
            ApplicationContext context =
                    new AnnotationConfigApplicationContext(BasicController.class);
            BasicService basicService = context.getBean(BasicService.class);
            basicService.testSaveUser();
        }
}
