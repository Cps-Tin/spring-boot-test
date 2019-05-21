package cn.cps.web;

import cn.cps.core.ResultGenerator;
import cn.cps.util.FileUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * 文件管理
 */
@Controller
public class FileController {

	String upLoadPath = "static"+File.separator+"upLoadFiles" + File.separator;

	/**
	 * 上传图片
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadImg")
	public String upload(HttpServletRequest request) {
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return JSON.toJSONString(FileUtil.upLoadFile(dirPath,request));
	}

	/**
	 * 文件下载
	 * @param fileName
	 */
	@ResponseBody
	@RequestMapping("/downLoadFile")
	public Object downLoadFile(String fileName,HttpServletRequest request, HttpServletResponse response) {
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return FileUtil.downLoadFile(fileName,dirPath,response);
	}

	/**
	 * 删除服务器文件
	 * @param fileName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/revomeFile")
	public Object revomeFile(String fileName,HttpServletRequest request){
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return ResultGenerator.genSuccessResult(FileUtil.removeFile(fileName,dirPath));
	}

}
