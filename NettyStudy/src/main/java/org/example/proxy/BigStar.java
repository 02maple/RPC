package org.example.proxy;

public class BigStar implements Star{
    private String name;

    public BigStar(String name){
        this.name=name;
    }

    public String sing(String s){
        System.out.println(this.name+"正在唱:"+s);
        return "谢谢大家";
    }

    public void dance(){
        System.out.println(this.name+"正在跳舞");
    }


}
