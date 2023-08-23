package org.maple.util;

public class ResponseUtil {
    public static Response CreateSuccessResult(){
        return new Response();
    }

    public static Response CreateFailResult(String status,String msg){
        Response result = new Response();
        result.setStatus(status);
        result.setMassage(msg);
        return result;
    }

    public static Response CreateSuccessResult(Object content){
        Response response = new Response();
        response.setResult(content);
        return response;
    }
}
