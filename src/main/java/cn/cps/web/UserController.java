package cn.cps.web;

import cn.cps.core.Result;
import cn.cps.core.ResultGenerator;
import cn.cps.entity.User;
import cn.cps.core.ResultPages;
import cn.cps.service.UserService;
import cn.cps.util.FileUtil;
import cn.cps.util.CheckCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.util.*;

/**
 * @author _Cps
 * @create 2019-02-14 10:24
 */
@Controller
@RequestMapping("/user")
public class UserController{

    @Resource
    public UserService userService;

    @ResponseBody
    @RequestMapping("/test")
    public Object test(HttpServletRequest request){
        // pageNum = 0 , pageSize = 0 表示查询全部
        PageHelper.startPage(0,0);
        List<User> list = userService.getUserList();
        PageInfo<User> pageInfo = new PageInfo<User>(list);

        User user = new User("_Cps","admin");
        ResultPages<User> baseResponse = new ResultPages<User>(pageInfo);
        return ResultGenerator.genSuccessResult(baseResponse);
    }


    @PostMapping("/doLogin")
    public String doLogin(User user, HttpSession session) {
        //在User中添加了个验证码字段
        if(user.getCheckCode()!=null && !user.getCheckCode().equals("")){
            String inputImageCode = user.getCheckCode();
            String sessionImageCode = (String) session.getAttribute(CheckCodeUtil.CHECK_CODE);
            session.removeAttribute(CheckCodeUtil.CHECK_CODE);//清除Session中的imageCode
            if(inputImageCode.equalsIgnoreCase(sessionImageCode)){
                User u = userService.doLogin(user);
                if (u != null) {
                    session.setAttribute("userSession",u);
                    return "redirect:/user/userList";
                }
            }else{
                System.err.println("验证码错误");
            }
        }
        System.err.println("登陆失败");
        return "login";
    }

    /**
     * 获取用户信息，并跳转页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Integer id,Model model) {
        User user = userService.getUser(id);
        if (user != null) {
            model.addAttribute("user",user);
        }
        return "update";
    }

    /**
     * 新增用户信息
     * @param user
     * @return
     */
    @PostMapping("/")
    public String addUser(User user) {
        Integer num = userService.addUser(user);
        if (num > 0) {
            return "redirect:/user/userList";
        }
        return "update";
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public String updateUser(User user) {
        System.out.println(user.getCreateDate());
        Integer num = userService.updUser(user);
        return "redirect:/user/userList";
    }

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public String delUser(@PathVariable("id") Integer id) {
        Integer num = userService.delUser(id);
        if (num > 0) {
            return "redirect:/user/userList";
        }
        return "index";
    }


    @RequestMapping("/userList")
    public String getUserList(@RequestParam(defaultValue = "1") Integer current,
                              @RequestParam(defaultValue = "5") Integer size, Model model) {
        PageHelper.startPage(current, size);//进行分页
        List<User> userList = userService.getUserList();//查询数据
        PageInfo<User> pageInfo = new PageInfo<User>(userList);//封装信息到PageInfo对象中（pagehelper提供的）
        Result result = ResultGenerator.genSuccessResult(new ResultPages<>(pageInfo));

        model.addAttribute("result",result );
        System.out.println(result);
        return "index";
    }

    /**
     * 生成前端的图片验证码
     */
    @RequestMapping(value = "/checkCode")
    public void getCheckCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            FileUtil.setResponseHeader("验证码",response);
            CheckCodeUtil randomValidateCode = new CheckCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            System.err.println("将内存中的图片通过流动形式输出到客户端失败>>>>");
        }
    }




}
