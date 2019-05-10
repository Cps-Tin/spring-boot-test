package cn.cps.service.impl;

import cn.cps.dao.UsersMapper;
import cn.cps.entity.Users;
import cn.cps.core.AbstractService;
import cn.cps.service.UsersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author _Cps
 * @create 2019-02-14 10:25
 */
@Service
public class UsersServiceImpl extends AbstractService<Users> implements UsersService {

    /*
        继承Mapper<T> 和 继承AbstractService<T> 两种选择一种就可以
        个人觉得继承AbstractService这种写法好一点
        因为在Controller中直接就可以调用封装好的方法，
        而Mapper还需要自己写Service的Impl，既然想用别人封装好的方法，为何不采用封装到底的AbstractService呢? 你说呢.
    */

    @Resource
    public UsersMapper usersMapper;

    public List<Users> getUsersList(){
        return usersMapper.selectAll();
    }


}
