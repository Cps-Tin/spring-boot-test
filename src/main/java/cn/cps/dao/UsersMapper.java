package cn.cps.dao;

import cn.cps.core.Mapper;
import cn.cps.entity.Users;

/**
 * @author _Cps
 * @create 2019-02-14 10:25
 * 使用通用的Mapper需要添加一些注解，并且自定义的Mapper继承通用Mapper<T>，它里面有一些增删改查的操作
 */
public interface UsersMapper extends Mapper<Users> {


}
