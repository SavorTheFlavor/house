package com.me;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HousesApplicationTests {

	@Autowired
	private HttpClient httpClient;

	@Test
	public void testHttpClient() throws IOException {
		String html = EntityUtils.toString(httpClient.execute(new HttpGet("https://www.juzimi.com/leisi/2688")).getEntity());
		System.out.println(html);
	}

}
