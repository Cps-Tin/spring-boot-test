package cn.cps.web;

import cn.cps.core.ResultGenerator;
import cn.cps.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * 图片管理接口
 * 
 * @author yyc
 *
 */
@Controller
public class FileController {

	String upLoadPath = "static"+File.separator+"upLoadFiles";

	@ResponseBody
	@RequestMapping(value = "/uploadImg")
	public Object upload(HttpServletRequest request) {
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return FileUtil.upLoadFile(dirPath,request);
	}

	/**
	 * 文件下载
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/downLoadFile")
	public Object downLoadFile(String fileName,HttpServletRequest request, HttpServletResponse response) {
		String dirPath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return FileUtil.downLoadFile(fileName,dirPath,request,response);
	}

	/**
	 * 删除服务器文件
	 * @param pathUrl
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/revomeFile")
	public Object revomeFile(String pathUrl,HttpServletRequest request){
		//String filePath = request.getSession().getServletContext().getRealPath(upLoadPath);
		return ResultGenerator.genSuccessResult(FileUtil.removeFile(File.separator+pathUrl));
	}

}
