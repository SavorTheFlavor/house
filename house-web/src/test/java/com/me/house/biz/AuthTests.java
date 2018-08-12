package com.me.house.biz;

import com.me.house.biz.service.UserService;
import com.me.house.common.model.User;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

// JUnit will invoke the class it references to run the tests in that class instead of the runner built into JUnit.
@RunWith(SpringRunner.class)
// 提供springboot的context
@SpringBootTest
public class AuthTests {

	@Autowired
	private UserService userService;

	@Test
	public void testAuth(){
		User user = userService.auth("root@qq.com", "23456");
		assert user != null;
		userService.enable("2084524121@qq.com");
	}

}
