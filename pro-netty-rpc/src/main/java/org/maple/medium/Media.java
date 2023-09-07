package org.maple.medium;

import com.alibaba.fastjson.JSONObject;
import org.maple.util.ServerRequest;
import org.maple.util.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Media {
    public static Map<String,BeanMethod> beanMethodMap;
    static {
        beanMethodMap = new HashMap<String, BeanMethod>();
    }

    private static Media m = null;
    private Media(){
    }
    public static Media newInstance(){
        if(m==null){
            m = new Media();
        }
        return m;
    }

    //反射处理业务
    //关键方法
    public Response process(ServerRequest serverRequest){
        Response result = null;
        try {
            String command = serverRequest.getCommand();
            BeanMethod beanMethod = beanMethodMap.get(command);
            if (beanMethod == null) {
                //说明接口请求异常
                return null;
            }
            //接口存在，正常进行
            Object bean = beanMethod.getBean();
            Method m = beanMethod.getM();
            Class<?> parameterType = m.getParameterTypes()[0];
            Object content = serverRequest.getContent();
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content), parameterType);

            result = (Response)m.invoke(bean, args);
            result.setId(serverRequest.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
