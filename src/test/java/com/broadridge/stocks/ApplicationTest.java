package com.broadridge.stocks;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.broadridge.stocks.controller.LoginController;
import com.broadridge.stocks.controller.ProductController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationTest.class);
	
	@Autowired
	private LoginController loginController;
	@Autowired
	private ProductController productController;
	
	@Test
    public void contextLoadsTest() throws Exception {
		assertThat(loginController).isNotNull();
		assertThat(productController).isNotNull();
    }


}
