package cn.cps.web;

import cn.cps.core.Result;
import cn.cps.core.ResultGenerator;
import cn.cps.entity.User;
import cn.cps.core.Response;
import cn.cps.service.UserService;
import cn.cps.util.BeanUtil;
import cn.cps.util.ExcelUtil;
import cn.cps.util.FileUtil;
import cn.cps.util.CheckCodeUtil;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
        Response<User> baseResponse = new Response<User>(pageInfo);
        return ResultGenerator.genSuccessResult(baseResponse);
    }


    //如果不给类添加RequestMapping("/user")的话,下面就是配置默认访问页面
    @GetMapping({"/", "/login.html"})
    public String index(Model model) {
        //重定向到 getUserList 请求
        return "login";
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

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Integer id,Model model) {
        User user = userService.getUser(id);
        if (user != null) {
            model.addAttribute("user",user);
            return "update";
        }
        return "redirect:/user/userList";
    }

    @PutMapping("/{id}")
    public String updateUser(User user) {
        System.out.println(user.getCreateDate());
        Integer num = userService.updUser(user);
        return "redirect:/user/userList";
    }

    @DeleteMapping("/{id}")
    public String delUser(@PathVariable("id") Integer id) {
        Integer num = userService.delUser(id);
        if (num > 0) {
            return "redirect:/user/userList";
        }
        return "index";
    }


    @RequestMapping("/userList")
    public String getUserList(@RequestParam(defaultValue = "1") Integer pageNo,
                              @RequestParam(defaultValue = "5") Integer size, Model model) {
        PageHelper.startPage(pageNo, size);//进行分页
        List<User> userList = userService.getUserList();//查询数据
        PageInfo<User> pageInfo = new PageInfo<User>(userList);//封装信息到PageInfo对象中（pagehelper提供的）
        Result result = ResultGenerator.genSuccessResult(new Response<>(pageInfo));

        model.addAttribute("result",result );
        System.out.println(result);
        return "index";
    }

    /**
     * 以流的方式导出报表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/export")
    public void export(HttpServletResponse response) throws Exception {

        //获取数据
        List<User> list = userService.getUserList();

        //excel标题
        String[] title = {"ID", "名称", "性别", "创建日期"};

        //实体类属性
        String[] field = {"id", "userName", "gender", "createDate"};

        //sheet名
        String sheetName = "学生信息表";

        //excel文件名
        String fileName = sheetName + LocalDate.now() + ".xls";

        //创建HSSFWorkbook
        HSSFWorkbook wb = new ExcelUtil().getHSSFWorkbook(sheetName, title, field , list);

        //响应到客户端
        try {
            FileUtil.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成前端的图片验证码
     */
    /**
     * 生成验证码
     */
    @RequestMapping(value = "/checkCode")
    public void getCheckCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            CheckCodeUtil randomValidateCode = new CheckCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            System.err.println("将内存中的图片通过流动形式输出到客户端失败>>>>");
        }
    }




}
