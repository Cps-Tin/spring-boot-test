package cn.cps.util;

import cn.cps.entity.User;
import org.apache.poi.hssf.record.formula.functions.T;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author _Cps
 * @create 2019-02-16 11:11
 */
public class ExcelUtil<T> {

    /**
     * 导出Excel
     * @return
     */
    public HSSFWorkbook getHSSFWorkbook(String sheetName, String []title, String [] field, List<T> list) throws Exception {

        //日期格式化对象
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String[][] content = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            Object obj = list.get(i);
//            //一个一个设置属性的方式，现在已经转变成属性数组自动封装内容
//            content[i][0] = obj.getId().toString();
//            content[i][1] = obj.getUserName();
//            content[i][2] = obj.getGender().toString();
//            content[i][3] = simpleDateFormat.format(obj.getCreateDate());
            //属性数组自动封装内容
            for (int j = 0; j < field.length; j++) {
                Object object = BeanUtil.getGetMethod(obj, field[j].toString());
                if (object instanceof Date) {
                    //处理日期格式
                    content[i][j] = simpleDateFormat.format(object);
                } else {
                    content[i][j] = object.toString();
                }
            }
        }
        // 1.创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        // 2.在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(18);

        // 3.在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(30);

        // 4.1.设置表头单元格样式
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont titleFont1 = wb.createFont();//字体
        titleFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
        style1.setFont(titleFont1);//设置到单元格中
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中格式
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

        // 4.2.设置内容单元格样式
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont titleFont2 = wb.createFont();//字体
        titleFont2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//字体正常不加粗
        style2.setFont(titleFont2);//设置到单元格中
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中格式
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

        //声明单元格对象
        HSSFCell cell = null;

        //创建标题,并设置样式
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellStyle(style1);
            cell.setCellValue(title[i]);
        }

        //创建内容，并设置样式
        for(int i=0;i<content.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<content[i].length;j++){
                //将内容按顺序赋给对应的列对象
                cell = row.createCell(j);
                cell.setCellStyle(style2);
                cell.setCellValue(content[i][j]);
            }
        }
        return wb;
    }



}
