package cn.cps.service;

import cn.cps.entity.User;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author _Cps
 * @create 2019-02-14 10:25
 */
public interface UserService{



    User doLogin(User user);

    Integer delUser(Integer id);

    User getUser(Integer id);

    Integer updUser(User user);
    
    List<User> getUserList();

    List<JSONObject> getUserListJSONObject();

    Integer addUser(User user);
}
