package com.cloud.oauth;

import com.cloud.model.user.LoginAppUser;
import com.cloud.oauth.feign.UserClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OauthTest {

    @Autowired
    private UserClient userClient;

    @Test
    public void findByUsername() {
        LoginAppUser loginAppUser = userClient.findByUsername("admin");
        System.out.println(loginAppUser);
    }

}
