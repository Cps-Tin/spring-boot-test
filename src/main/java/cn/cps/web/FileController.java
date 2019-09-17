package cn.cps.web;

import cn.cps.core.Result;
import cn.cps.core.ResultGenerator;
import cn.cps.entity.User;
import cn.cps.service.UserService;
import cn.cps.util.FileUtil;
import cn.cps.util.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;


/**
 * 文件管理
 */
@RestController
@RequestMapping("/file")
public class FileController {

	@Value("${web.upload-path}")
	private String upLoadPath;

	@Resource
	public UserService userService;

	//如果不启用WebMvcConfigurer，不给类添加RequestMapping("/user")的话,下面就是配置默认访问页面
	@GetMapping({"/", "/login.html"})
	public String index(Model model) {
		//重定向到 getUserList 请求
		return "login";
	}

	/**
	 * 上传图片
	 * @param request
	 */
	@RequestMapping(value = "/upLoadImg")
	public Result upLoadImg(HttpServletRequest request) {
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return FileUtil.upLoadFile(dirPath,request);
	}

	/**
	 * 文件下载
	 * @param fileName
	 */
	@RequestMapping("/downLoadFile")
	public Result downLoadFile(String fileName,HttpServletRequest request, HttpServletResponse response) {
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return FileUtil.downLoadFile(fileName,dirPath,response);
	}

	/**
	 * 删除服务器文件
	 * @param fileName
	 * @return
	 */
	@RequestMapping("/revomeFile")
	public Result revomeFile(String fileName,HttpServletRequest request){
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return ResultGenerator.genSuccessResult(FileUtil.removeFile(fileName,dirPath));
	}

	/**
	 * Excel导入
	 * @param file
	 * @param response
	 */
	@RequestMapping("/excel/import")
	public Result importExcel(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException, InstantiationException, IllegalAccessException {
		InputStream inputStream = file.getInputStream();
		List<User> list = ExcelUtil.ImportExcel(inputStream, User.class);
		System.out.println(list);
		return ResultGenerator.genSuccessResult();
	}

	/**
	 * Excel导出
	 * @param response
	 */
	@RequestMapping("/excel/export")
	public void exportExcel(HttpServletResponse response) throws IOException, InstantiationException, IllegalAccessException {
		List<User> userList = userService.getUserList();
		ExcelUtil.ExportExcel("人员信息_"+UUID.randomUUID().toString(), response, userList, User.class);
	}



}
