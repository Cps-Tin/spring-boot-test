package cn.cps.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
     * 封装数据并导出Excel
     * @return
     */
    public HSSFWorkbook getHSSFWorkbook(ExcelMake make, JSONObject data, List<JSONObject> list) throws Exception {

        //日期格式化对象
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        HSSFWorkbook wb = make.getHssfWorkbook();

        Iterator iterator = data.keySet().iterator();

        JSONArray props = data.getJSONArray("props");

        //遍历行---封装表头信息
        while (iterator.hasNext()) {
            //我们只要row的信息---也就是出了props和sheetName
            String key = iterator.next().toString();
            if (!"props".equalsIgnoreCase(key) && !"sheetName".equalsIgnoreCase(key)) {
                make.createRow();
                JSONArray cells = data.getJSONArray(key);
                for (int i = 0; i < cells.size(); i++) {
                    JSONObject cell = cells.getJSONObject(i);
                    make.createCell(cell.getInteger("width"),
                            cell.getInteger("height")).
                            setCellValue(cell.getString("name"));
                }
            }
        }
        //遍历行---封装数据信息
        String[][] content = new String[list.size()][props.size()];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[props.size()];
            JSONObject obj = list.get(i);
            for (int j = 0; j < props.size(); j++) {
                Object object = obj.get(props.get(j));
                if(object!=null){
                    if (object instanceof Date) {
                        //处理日期格式
                        content[i][j] = simpleDateFormat.format(object);
                    } else {
                        content[i][j] = object.toString();
                    }
                }else{
                    content[i][j] = "";
                }
            }
        }
        // 获取ExcelMake之前创建的个sheet
        HSSFSheet sheet = make.getHssfWorkbook().getSheet(make.getName());

        // 在sheet中表头的下方添加数据
        for (int i = 0; i < content.length; i++) {
            make.createRow();
            for (int j = 0; j < content[i].length; j++) {
                make.createCell().setCellValue(content[i][j]);
            }
        }
        return wb;
    }




}
