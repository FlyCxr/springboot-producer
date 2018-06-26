package com.springboot;

import com.alibaba.fastjson.JSONObject;
import com.springboot.entity.DrvierEntity;
import com.springboot.redis.RedisSingleClient;
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
 *
 * 不进行的可以关闭，System.err红色输出
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("all")
public class DemoApplicationTests {

	@Autowired
	private DrvierService drvierService;

	@Autowired
	private RedisSingleClient redisSingleClient;

	@Test
	public void contextLoads() {
		/**
		 * 根据手机号查询司机
		 */
		DrvierEntity entity = drvierService.getDrvierByPhoneNum("15000814778");
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
        entity3.setDrvierPhone("15000814783");
        entity3.setDrvierName("dongyangyang777");
        Integer add = drvierService.add(entity3);
        System.err.println(add);

        /**
         * 删除，批量删除，物理删除
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

		//设置超时时间
		redisSingleClient.expire("dongyangyang",30 * 60);

		//取值
		String s = redisSingleClient.get("123");
		System.err.println(s);

		//设值
		String setValue = redisSingleClient.set("123", "123456", 30 * 60);
		System.err.println(setValue);

		//取值
		String a = redisSingleClient.get("123");
		System.err.println(a);

		//设置超时时间
		redisSingleClient.expire("dongyangyang",10);

		//是否存在
		boolean b = redisSingleClient.exists("123");
		System.err.println(b);

		//删除key
		long remove = redisSingleClient.remove("123");
		System.err.println(remove);

		//删除key
		long remove2 = redisSingleClient.remove("123");
		System.err.println(remove2);

		//是否存在
		boolean b2 = redisSingleClient.exists("123");
		System.err.println(b2);

		//
		long setnx = redisSingleClient.setnx("suo", "123");
		System.err.println(setnx);
		long setnx2 = redisSingleClient.setnx("suo", "123");
		System.err.println(setnx2);
		long setnx3 = redisSingleClient.setnx("suo", "123");
		System.err.println(setnx3);

		DrvierEntity entity7 = new DrvierEntity();
		entity7.setDrvierPhone("15000819999");
		entity7.setDrvierName("董杨炀");
		String result = redisSingleClient.setObject("哈哈", entity7, 0);
		System.err.println(result);

		DrvierEntity vo = (DrvierEntity)redisSingleClient.getObject("哈哈");
		System.err.println(vo.getDrvierName());
		System.err.println(vo.getDrvierPhone());

		redisSingleClient.expire("哈哈",10);

		boolean e = redisSingleClient.exists("哈哈");
		System.err.println(e);

		boolean b1 = redisSingleClient.existsObject("哈哈");
		System.err.println(b1);

		redisSingleClient.removeObject("哈哈");

		boolean c = redisSingleClient.existsObject("哈哈");
		System.err.println(c);

		boolean d = redisSingleClient.exists("哈哈");
		System.err.println(d);


		List<String> strs = new ArrayList();
		strs.add("123");
		strs.add("456");
		long a1 = redisSingleClient.setList("strs2", strs, 0);
		System.err.println(a1);
		List<String> strs2 = redisSingleClient.getList("strs2");
		System.err.println(strs2);

		long strs21 = redisSingleClient.listAdd("strs2", "789", "110");
		System.err.println(strs21);
		List<String> strs3 = redisSingleClient.getList("strs2");
		System.err.println(strs3);


		List list5 = new ArrayList();
		DrvierEntity entity8 = new DrvierEntity();
		entity8.setDrvierPhone("15000819999");
		entity8.setDrvierName("董杨炀");
		DrvierEntity entity9 = new DrvierEntity();
		entity9.setDrvierPhone("15000819998");
		entity9.setDrvierName("董杨炀2");
		list.add(entity8);
		list.add(entity9);
		long drviers = redisSingleClient.setObjectList("drviers2", list5, 0);
		System.err.println(drviers);


		List<Object> drviers1 = redisSingleClient.getObjectList("drviers2");
		for (int i = 0; i < drviers1.size() ; i++) {
			DrvierEntity entity10 =(DrvierEntity)drviers1.get(i);
			System.err.println(entity10.getDrvierPhone());
			System.err.println(entity10.getDrvierName());
		}

		DrvierEntity entity31 = new DrvierEntity();
		entity31.setDrvierPhone("15000819998");
		entity31.setDrvierName("董杨炀3");

		DrvierEntity entity4 = new DrvierEntity();
		entity4.setDrvierPhone("15000819998");
		entity4.setDrvierName("董杨炀4");

		long drviers2 = redisSingleClient.listObjectAdd("drviers2",entity31,entity4);
		System.err.println(drviers2);

		List<Object> drviers3 = redisSingleClient.getObjectList("drviers2");
		for (int i = 0; i < drviers3.size() ; i++) {
			DrvierEntity entity5 =(DrvierEntity)drviers3.get(i);
			System.err.println(entity5.getDrvierPhone());
			System.err.println(entity5.getDrvierName());
		}
	}

}
