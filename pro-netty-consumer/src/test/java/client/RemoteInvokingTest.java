package client;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maple.annotation.RemoteInvoke;
import org.maple2.client.param.Response;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.maple2.user.remote.UserRemote;
import org.maple2.user.bean.User;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokingTest.class)
@ComponentScan("\\")
public class RemoteInvokingTest {
    @RemoteInvoke
    private UserRemote userRemote;


    @Test
    public void testSaveUser(){
        User user = new User();
        user.setId(1);
        user.setName("张三");
        Response response = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void testSaveUsers(){
        List<User> userList = new ArrayList<User>();
        User user = new User();
        user.setId(1);
        user.setName("张三");
        userList.add(user);
        userRemote.saveUsers(userList);
    }

}
