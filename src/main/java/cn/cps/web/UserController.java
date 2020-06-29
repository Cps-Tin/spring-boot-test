package cn.cps.web;

import cn.cps.core.Result;
import cn.cps.core.ResultGenerator;
import cn.cps.entity.User;
import cn.cps.core.ResultPages;
import cn.cps.service.UserService;
import cn.cps.util.ExcelMake;
import cn.cps.util.ExcelUtil;
import cn.cps.util.FileUtil;
import cn.cps.util.VerifyCodeUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author _Cps
 * @create 2019-02-14 10:24
 * @desc 使用自定义mapper接口
 */
@Api(tags="用户接口")
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
    @ApiOperation(value = "用户登录")
    public String doLogin(User user, HttpSession session) {
        //在User中添加了个验证码字段
        if(user.getVerifyCode()!=null && !user.getVerifyCode().equals("")){
            String inputImageCode = user.getVerifyCode();
            String sessionImageCode = (String) session.getAttribute(VerifyCodeUtils.VERIFY_CODE);
            if(inputImageCode.equalsIgnoreCase(sessionImageCode)){
                User u = userService.doLogin(user);
                if (u != null) {
                    session.removeAttribute(VerifyCodeUtils.VERIFY_CODE);//清除Session中的imageCode
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
     * 以流的方式导出报表(支持多表头)，使用说明参考doc目录下的ExcelPlus导出报表.pdf
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/export")
    public void export(HttpServletResponse response) throws Exception {

        //需要的参数：ExcelMake对象、JSONOject对象、list数据

        //sheet名
        String sheetName = "用户信息";

        //属性配置
        String props = "['id', 'userName', 'genderName', 'createDate']";

        //表头配置
        String row1 = "[{width:'1',height:'3',name:'结算部门'},{width:'1',height:'3',name:'类型'},{width:'6',height:'1',name:'公司账户'}]";
        String row2 = "[{width:'3',height:'1',name:'点心'},{width:'3',height:'1',name:'套餐'}]";
        String row3 = "[{width:'1',height:'1',name:'刷卡次数'},{width:'1',height:'1',name:'单价'},{width:'1',height:'1',name:'总金额'},{width:'1',height:'1',name:'次数'},{width:'1',height:'1',name:'单价'},{width:'1',height:'1',name:'总金额'}]";

        //封装成JSONObject对象
        JSONObject data = new JSONObject(){};
        data.put("1",row1);
        data.put("2",row2);
        data.put("3",row3);
        data.put("props",props);
        data.put("sheetName",sheetName);

        //获取数据---为什么是JSONObject对象，看Mapper.xml就理解了
        List<JSONObject> list = userService.getUserListJSONObject();

        //excel文件名
        String fileName = sheetName + System.currentTimeMillis() + ".xls";

        //绘制单元格对象
        ExcelMake make = new ExcelMake(sheetName);

        //创建HSSFWorkbook
        HSSFWorkbook wb = new ExcelUtil().getHSSFWorkbook(make, data , list);

        //响应到客户端
        try {
            FileUtil.setResponseHeader(fileName,response);
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
    @ResponseBody
    @GetMapping(value = "/verifyCode")
    @ApiOperation(value = "生成前端的图片验证码", httpMethod = "GET", protocols = "HTTP", notes = "生成前端的图片验证码")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int width = 120;//宽
        int height = 40;//高
        int verifySize = 4;//验证码个数

        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(verifySize);
        System.err.println("验证码:"+verifyCode);
        //存入会话session
        request.getSession().setAttribute(VerifyCodeUtils.VERIFY_CODE, verifyCode.toLowerCase());
        //生成图片
        VerifyCodeUtils.outputImage(width, height, response.getOutputStream(), verifyCode);
    }

    /**
     * 生成前端的图片验证码Base64
     */
    @ResponseBody
    @RequestMapping(value = "/verifyCodeBase64")
    @ApiOperation(value = "生成前端的图片验证码Base64", httpMethod = "POST", protocols = "HTTP", notes = "生成前端的图片验证码Base64", produces = "application/txt")
    public String  verifyCodeBase64(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int width = 120;//宽
        int height = 40;//高
        int verifySize = 4;//验证码个数

        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(verifySize);
        //存入会话session
        request.getSession().setAttribute(VerifyCodeUtils.VERIFY_CODE, verifyCode.toLowerCase());
        //生成图片
        String imgBase64 = VerifyCodeUtils.outputImageBase64(width, height, verifyCode);

        return imgBase64;
    }




}
