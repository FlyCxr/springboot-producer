package com.springboot;

import com.springboot.entity.DrvierEntity;
import com.springboot.service.DrvierService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试例子。测试Service
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private DrvierService drvierService;

	@Test
	public void contextLoads() {
		DrvierEntity entity = drvierService.getDrvierByPhoneNum("15000814725");
		System.err.println(entity);
	}

}
