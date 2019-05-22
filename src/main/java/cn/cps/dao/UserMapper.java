package cn.cps.dao;

import cn.cps.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author _Cps
 * @create 2019-02-14 10:25
 */
public interface UserMapper {


    User doLogin(User user);

    Integer delUser(@Param("id") Integer id);

    User getUser(@Param("id")Integer id);

    Integer updUser(User user);

    List<User> getUserList();

    List<JSONObject> getUserListJSONObject();

    Integer addUser(User user);
}
