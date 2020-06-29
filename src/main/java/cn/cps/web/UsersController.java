package cn.cps.web;

import cn.cps.dao.UsersMapper;
import cn.cps.service.UsersService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author _Cps
 * @create 2019-02-14 10:24
 * @desc 使用MyBatis Mapper插件的Mapper接口
 */
@Controller
@RequestMapping("/users")
public class UsersController {

    @Resource
    public UsersMapper usersMapper;

    @Resource
    public UsersService usersService;

    /**
     * @desc 测试通用 MyBatis Mapper插件的Mapper接口
     * @return
     */
    @ResponseBody
    @RequestMapping("/testMapperInterface")
    public String testMapperInterface(){
        return JSON.toJSONString(usersMapper.selectAll());
    }

    /**
     * @desc 测试通用 MyBatis Mapper插件的Service接口
     * @return
     */
    @ResponseBody
    @RequestMapping("/testServiceInterface")
    public String testServiceInterface(){
        return JSON.toJSONString(usersService.findAll());
    }




}
