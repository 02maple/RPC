package org.maple.users.service;
import org.maple.users.bean.User;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserService {
    public void save(User user){
        //访问mysql...
        System.out.println("invoke method:save()");
    }

    public void saveList(List<User> users) {
        System.out.println("invoke method:saveList()");
    }
}
