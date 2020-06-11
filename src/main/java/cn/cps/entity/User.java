package cn.cps.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author _Cps
 * @create 2019-02-14 10:12
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @ApiModelProperty(value = "用户ID")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户性别")
    private Integer gender;

    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")处理JSON格式日期问题
    //这里使用了JSONArray.toJSONStringWithDateFormat()处理日期问题，在Result类中
    private Date createDate;

    @ApiModelProperty(value = "验证码")
    private String verifyCode;


    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


}
