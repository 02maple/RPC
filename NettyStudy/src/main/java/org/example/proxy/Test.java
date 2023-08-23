package org.example.proxy;

public class Test {
    public static void main(String[] args) {
            BigStar star = new BigStar("张三");
            Star proxy = ProxyUtil.creatProxy(star);
            String s = proxy.sing("好运来");
            System.out.println(s);


    }
}
