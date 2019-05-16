package cn.cps.util;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author _Cps
 * @create 2019-02-16 11:11
 */
public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String []title, String [][]values){

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
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                cell = row.createCell(j);
                cell.setCellStyle(style2);
                cell.setCellValue(values[i][j]);
            }
        }
        return wb;
    }



}
