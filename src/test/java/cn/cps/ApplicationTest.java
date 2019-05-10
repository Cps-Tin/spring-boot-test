package cn.cps;

import cn.cps.core.ResultGenerator;
import cn.cps.service.UserService;
import cn.cps.util.MD5;
import cn.cps.util.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {


    @Test
    public void contextLoads() {

        System.out.println(MD5.getMD5String("_Cps"));
        System.out.println(StringUtil.getNewOrderNo());

    }

}

