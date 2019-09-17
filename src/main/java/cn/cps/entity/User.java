package cn.cps.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author _Cps
 * @create 2019-02-14 10:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseRowModel { //BaseRowModel 阿里的Excel导入导出

    @ExcelProperty(value="ID",index=0)
    private Integer id;

    @ExcelProperty(value="姓名",index=1)
    private String userName;

    @ExcelProperty(value="密码",index=2)
    private String password;

    @ExcelProperty(value="性别",index=3)
    private Integer gender;

    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")处理JSON格式日期问题
    //这里使用了JSONArray.toJSONStringWithDateFormat()处理日期问题，在Result类中
    @ExcelProperty(value="出生日期",index=4)
    private Date createDate;

    private String checkCode;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", createDate=" + createDate +
                ", checkCode=" + checkCode +
                '}';
    }

}
