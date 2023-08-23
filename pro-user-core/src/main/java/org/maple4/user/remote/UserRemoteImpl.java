package org.maple4.user.remote;

import org.maple.annotation.Remote;
import org.maple3.user.model.User;
//import org.maple.users.remote.UserRemote;
//import org.maple.users.service.UserService;
import org.maple.util.ResponseUtil;
import org.maple3.user.remote.UserRemote;
import org.maple4.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote {
    @Resource
    private UserService userService;
    public Object saveUser(User user){
        userService.save(user);
        return ResponseUtil.CreateSuccessResult(user);
    }
    public Object saveUsers(List<User> users){
        userService.saveList(users);
        return ResponseUtil.CreateSuccessResult(users);
    }
}
