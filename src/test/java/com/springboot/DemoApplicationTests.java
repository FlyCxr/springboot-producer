package com.springboot;

import com.alibaba.fastjson.JSONObject;
import com.springboot.entity.DrvierEntity;
import com.springboot.service.DrvierService;
import com.springboot.vo.BaseVo;
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
		/**
		 * 根据手机号查询司机
		 */
		DrvierEntity entity = drvierService.getDrvierByPhoneNum("15000814725");
		BaseVo vo1 = new BaseVo();
		vo1.setData(entity);
		System.err.println(JSONObject.toJSONString(vo1));

		/**
		 * 分页查询司机
		 */
		DrvierEntity entity2 = new DrvierEntity();
		entity2.setPage(2);
		entity2.setPageSize(20);
		JSONObject object = drvierService.getPage(entity2);
		BaseVo vo2 = new BaseVo();
		vo2.setData(object);
		System.err.println(JSONObject.toJSONString(vo2));
	}

}
