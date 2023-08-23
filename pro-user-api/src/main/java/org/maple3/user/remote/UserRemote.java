package org.maple3.user.remote;



import org.maple3.user.model.User;

import java.util.List;

public interface UserRemote {
    public Object saveUser(User user);
    public Object saveUsers(List<User> users);
}
