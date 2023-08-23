package org.maple2.user.remote;

import org.maple2.client.param.Response;
import org.maple2.user.bean.User;


import java.util.List;

public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> users);
}
