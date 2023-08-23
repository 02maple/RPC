package org.maple.users.remote;

import org.maple.annotation.Remote;
import org.maple.users.bean.User;
import org.maple.users.service.UserService;
import org.maple.util.Response;
import org.maple.util.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote{
    @Resource
    private UserService userService;
    public Response saveUser(User user){
        userService.save(user);
        return ResponseUtil.CreateSuccessResult(user);
    }
    public Response saveUsers(List<User> users){
        userService.saveList(users);
        return ResponseUtil.CreateSuccessResult(users);
    }
}
