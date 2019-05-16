package cn.cps;

import cn.cps.util.AESUtil;
import cn.cps.util.BirthUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {


    @Test
    public void contextLoads() {

//        System.out.println(AESUtil.encrypt("Cps","123456789"));
//        System.out.println(AESUtil.decrypt("kaqkY/pR6B9+q5t08Qe+HQ==","123456789"));

//        System.out.println(MD5.getMD5String("_Cps"));
//        System.out.println(StringUtil.getNewOrderNo());

        System.out.println(BirthUtil.getSex("410526199912120119"));
        System.out.println(BirthUtil.getZodica("410526199912120119"));
        System.out.println(BirthUtil.getConstellation("410526199912120119"));

    }

}

