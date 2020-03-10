package com.test.dockertest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.test.dockertest.controller.HomeController;

@SpringBootTest
class DockerTestApplicationTests {
	
	@Autowired
	private HomeController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
		
	}
	
	@Test
	public void shouldReturnDefaultMessage() throws Exception {
        String result = controller.index();
        assertEquals(result, "<h1>Hello From Spring Boot!!</h1>");
	}

}
