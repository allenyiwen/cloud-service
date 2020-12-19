package com.cloud.log;

import org.junit.Test;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class LogTest {

    /**
     * 2019.07.29之前的t_log数据可运行此test生成update语句修复一下
     */
    @SuppressWarnings("deprecation")
    @Test
    public void updateSql() {
        com.cloud.model.log.constants.LogModule.MODULES.forEach((k, v) -> {
            System.out.println("update t_log t set t.module = '" + v + "' where t.module = '" + k + "';");
        });

    }

}
