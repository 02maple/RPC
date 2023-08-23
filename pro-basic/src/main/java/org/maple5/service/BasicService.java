package org.maple5.service;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.maple.annotation.RemoteInvoke;
import org.maple2.client.param.Response;
//import org.maple2.user.bean.User;
//import org.maple2.user.remote.UserRemote;
import org.maple3.user.model.User;
import org.maple3.user.remote.UserRemote;
import org.springframework.stereotype.Service;

@Service
public class BasicService {

    @RemoteInvoke
    private UserRemote userRemote;
    public void testSaveUser(){
        User user = new User();
        user.setId(1);
        user.setName("张三");
        Object response = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(response));
    }
}
