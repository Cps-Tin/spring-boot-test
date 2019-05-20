package cn.cps.util;

import cn.cps.entity.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.record.formula.functions.T;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
    public HSSFWorkbook getHSSFWorkbook(ExcelMake make, JSONObject sheetJSONObject, String [] props, List<JSONObject> list) throws Exception {

        //日期格式化对象
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        HSSFWorkbook wb = make.getHssfWorkbook();

        Iterator iterator = sheetJSONObject.keySet().iterator();

        //遍历行---封装表头信息
        while(iterator.hasNext()){
            make.createRow();
            JSONArray cells = JSONArray.parseArray(sheetJSONObject.get(iterator.next()).toString());
            for (int i = 0; i < cells.size(); i++) {
                JSONObject cell = JSONObject.parseObject(cells.get(i).toString());
                if("".equals(cell.get("width").toString()) || "".equals(cell.get("height").toString())){
                    make.createCell().
                            setCellValue(cell.get("name").toString());
                }else{
                    make.createCell(Integer.valueOf(cell.get("width").toString()),
                            Integer.valueOf(cell.get("height").toString())).
                            setCellValue(cell.get("name").toString());
                }
            }
        }

        //遍历行---封装数据信息
        String[][] content = new String[list.size()][props.length];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[props.length];
            JSONObject obj = list.get(i);
            for (int j = 0; j < props.length; j++) {
                Object object = obj.get(props[j]);
                if (object instanceof Date) {
                    //处理日期格式
                    content[i][j] = simpleDateFormat.format(object);
                } else {
                    content[i][j] = object.toString();
                }
            }
        }
        // 获取ExcelMake之前创建的个sheet
        HSSFSheet sheet = make.getHssfWorkbook().getSheet(make.getName());

        // 在sheet中表头的下方添加数据
        for(int i=0;i<content.length;i++){
            make.createRow();
            for(int j=0;j<content[i].length;j++){
                make.createCell().setCellValue(content[i][j]);
            }
        }
        return wb;
    }



}
