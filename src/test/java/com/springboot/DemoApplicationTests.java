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

import java.util.ArrayList;
import java.util.List;

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
        entity2.setDrvierPhone("15000814726");//设置普通条件
		entity2.setPage(2);
		entity2.setPageSize(20);
		JSONObject object = drvierService.getPage(entity2);
		BaseVo vo2 = new BaseVo();
		vo2.setData(object);
		System.err.println(JSONObject.toJSONString(vo2));

        /**
         * 新增实体
         */
        DrvierEntity entity3 = new DrvierEntity();
        entity3.setVersion(0);
        entity3.setDrvierPhone("15000814777");
        entity3.setDrvierName("dongyangyang777");
        Integer add = drvierService.add(entity3);
        System.err.println(add);

        /**
         * 删除，批量删除
         */
        List list = new ArrayList();
        list.add(39);
        list.add(44);
        list.add(46);
        Integer delete = drvierService.delete(list, DrvierEntity.class);
        System.err.println(delete);

        /**
         * 修改实体，必须重写
         * TODO 表字段设计版本号，必须更新修改人，修改时间，版本自增，其余字段或者条件更新
         * 可以参考
         * drvierService.updateDrvier();
         */
    }

}
