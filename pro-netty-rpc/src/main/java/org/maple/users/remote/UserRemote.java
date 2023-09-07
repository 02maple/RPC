package org.maple.users.remote;

import org.maple.users.bean.User;
import org.maple.util.Response;
import java.util.List;

public interface UserRemote {
    Response saveUser(User user);
    Response saveUsers(List<User> users);
}
