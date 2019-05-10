package cn.cps.service;

import cn.cps.entity.User;

import java.util.List;

/**
 * @author _Cps
 * @create 2019-02-14 10:25
 */
public interface UserService{


    List<User> getUserList();

    User doLogin(User user);

    Integer delUser(Integer id);

    User getUser(Integer id);

    Integer updUser(User user);
}
