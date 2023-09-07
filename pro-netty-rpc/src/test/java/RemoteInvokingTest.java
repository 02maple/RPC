import org.junit.Test;
import org.junit.runner.RunWith;
import org.maple.annotation.RemoteInvoke;
import org.maple.users.bean.User;
import org.maple.users.remote.UserRemote;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokingTest.class)
@ComponentScan("org.maple")
public class RemoteInvokingTest {
    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void testSaveUser(){
        User user = new User();
        user.setId(1);
        user.setName("张三");
        userRemote.saveUser(user);
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
