package cn.cps.service.impl;

import cn.cps.dao.UserMapper;
import cn.cps.dao.UsersMapper;
import cn.cps.entity.User;
import cn.cps.service.UserService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author _Cps
 * @create 2019-02-14 10:25
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    public UserMapper userMapper;

    @Resource
    public UsersMapper usersMapper;

    @Override
    public List<User> getUserList() {
        System.out.println(JSONArray.toJSONString(usersMapper.selectAll()));;
        return userMapper.getUserList();
    }

    @Override
    public List<JSONObject> getUserListJSONObject() {
        return userMapper.getUserListJSONObject();
    }

    @Override
    public Integer addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public User doLogin(User user) {
        return userMapper.doLogin(user);
    }

    @Override
    public Integer delUser(Integer id) {
        return userMapper.delUser(id);
    }

    @Override
    public User getUser(Integer id) {
        return userMapper.getUser(id);
    }

    @Override
    public Integer updUser(User user) {
        return userMapper.updUser(user);
    }
}
