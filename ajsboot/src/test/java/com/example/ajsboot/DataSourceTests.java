package com.example.ajsboot;


import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.sql.Connection;

@SpringBootTest
@Log4j2
public class DataSourceTests {
	@Autowired
	private DataSource dataSource;
	
	@Test
	public void testConnection() throws Exception{
		@Cleanup
		Connection con = dataSource.getConnection();
	
		System.out.println("Con  : "+ con);
		Assertions.assertNotNull(con);
	}
}
