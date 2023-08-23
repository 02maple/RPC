//import org.junit.Test;
//import org.maple.client.ClientRequest;
//import org.maple.util.Response;
//import org.maple.client.TCPClient;
//import org.maple2.user.bean.User;
////import org.maple.users.bean.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestTcp {
//
//    @Test
//    public void testGetResponse() throws InterruptedException {
//        ClientRequest clientRequest = new ClientRequest();
//        clientRequest.setContent("测试tcp长连接");
//        Response response = TCPClient.send(clientRequest);
//        System.out.println(response.getResult());
//    }
//
//    @Test
//    public void testSaveUser() throws InterruptedException {
//        ClientRequest clientRequest = new ClientRequest();
//        User user = new User();
//        user.setId(1);
//        user.setName("张三");
//
//        clientRequest.setContent(user);
//        clientRequest.setCommand("org.maple.users.controller.UserController.saveUser");
//
//        Response response = TCPClient.send(clientRequest);
//        System.out.println(response.getResult());
//
//    }
//
//    @Test
//    public void testSaveUsers() throws InterruptedException {
//        ClientRequest clientRequest = new ClientRequest();
//        List<User> userList = new ArrayList<User>();
//        User user = new User();
//        user.setId(1);
//        user.setName("张三");
//        userList.add(user);
//
//        clientRequest.setContent(userList);
//        clientRequest.setCommand("org.maple.users.controller.UserController.saveUsers");
//
//        Response response = TCPClient.send(clientRequest);
//        System.out.println(response.getResult());
//
//    }
//}
