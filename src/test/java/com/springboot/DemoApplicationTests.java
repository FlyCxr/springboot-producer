package com.springboot;

import com.alibaba.fastjson.JSONObject;
import com.springboot.config.HttpClientApi;
import com.springboot.entity.DrvierEntity;
import com.springboot.redis.RedisSingleClient;
import com.springboot.service.DrvierService;
import com.springboot.vo.BaseVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

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

	@Autowired
	private HttpClientApi httpClientApi;

	@Test
	public void contextLoads() throws Exception{

		String baidu = httpClientApi.doGet("http://www.baidu.com/");
		System.out.println(baidu);

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
		long remove = redisSingleClient.delete("123");
		System.err.println(remove);

		//删除key
		long remove2 = redisSingleClient.delete("123");
		System.err.println(remove2);

		//是否存在
		boolean b2 = redisSingleClient.exists("123");
		System.err.println(b2);

		//setnx锁模拟测试
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

		redisSingleClient.deleteObject("哈哈");

		boolean c = redisSingleClient.existsObject("哈哈");
		System.err.println(c);

		boolean d = redisSingleClient.exists("哈哈");
		System.err.println(d);


		//测试Redis 对List的支持
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
		list5.add(entity8);
		list5.add(entity9);
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

		//测试redis Set操作API
		Set<String> set = new HashSet();
		set.add("789");
		set.add("456");
		long set1 = redisSingleClient.setSet("set", set, 0);
		System.err.println(set1);

		long set2 = redisSingleClient.setAdd("set","123");
		System.err.println(set2);

		DrvierEntity s1 = new DrvierEntity();
		s1.setDrvierPhone("15000819998");
		s1.setDrvierName("董杨炀1");
		DrvierEntity s2 = new DrvierEntity();
		s2.setDrvierPhone("15000819998");
		s2.setDrvierName("董杨炀2");
		DrvierEntity s3 = new DrvierEntity();
		s3.setDrvierPhone("15000819997");
		s3.setDrvierName("董杨炀3");
		Set set3 = new HashSet();
		set3.add(s1);
		set3.add(s2);
		long s4 = redisSingleClient.setObjectSet("set3", set3, 0);
		System.err.println(s4);

		long s5 = redisSingleClient.setObjectAdd("set3",s3);
		System.err.println(s5);

		Set<String> set4 = redisSingleClient.getSet("set");
		for (String o: set4) {
			System.err.println(o);
		}

		Set<Object> set5 = redisSingleClient.getObjectSet("set3");
		for (Object o: set5) {
			DrvierEntity e3 = (DrvierEntity)o;
			System.err.println(e3.getDrvierPhone());
			System.err.println(e3.getDrvierName());
		}

		//redis对Map的支持
		Map<String,String> map = new HashMap<>();
		map.put("a","a");
		map.put("b","b");
		map.put("c","c");
		String map1 = redisSingleClient.setMap("map", map, 0);
		System.err.println(map1);

		Map<String,String> map2 = new HashMap<>();
		map2.put("d","d");

		String map3 = redisSingleClient.mapPut("map", map2);
		System.err.println(map3);

		//判断map的key是否在Map缓存中存在
		boolean b5 = redisSingleClient.mapExists("map", "a");
		System.err.println(b5);

		redisSingleClient.mapRemove("map","a");

		boolean b6 = redisSingleClient.mapExists("map", "a");
		System.err.println(b6);

		Map<String, String> map4 = redisSingleClient.getMap("map");
		for (String s9 : map4.values()) {
			System.err.println(s9);
		}

		DrvierEntity m1 = new DrvierEntity();
		m1.setDrvierPhone("15000819998");
		m1.setDrvierName("董杨炀1");
		DrvierEntity m2 = new DrvierEntity();
		m2.setDrvierPhone("15000819998");
		m2.setDrvierName("董杨炀2");
		DrvierEntity m3 = new DrvierEntity();
		m3.setDrvierPhone("15000819998");
		m3.setDrvierName("董杨炀3");
		Map<String,Object> objMap = new HashMap<>();
		Map<String,Object> objMap2 = new HashMap<>();
		objMap.put("a",m1);
		objMap.put("b",m2);
		objMap2.put("c",m3);
		String r1 = redisSingleClient.setObjectMap("objMap", objMap, 0);
		System.err.println(r1);

		String r2 = redisSingleClient.mapObjectPut("objMap", objMap2);
		System.err.println(r2);

		boolean b3 = redisSingleClient.mapObjectExists("objMap","a");
		System.err.println(b3);

		redisSingleClient.mapObjectRemove("objMap","a");

		boolean b4 = redisSingleClient.mapObjectExists("objMap","a");
		System.err.println(b4);

		Map<String, Object> objectMap = redisSingleClient.getObjectMap("objMap");

		for (Object s9 : objectMap.values()) {
			DrvierEntity m4 = (DrvierEntity)s9;
			System.err.println(m4.getDrvierPhone());
			System.err.println(m4.getDrvierName());
		}


		//redis锁测试
		DrvierEntity entity11 = new DrvierEntity();
		entity11.setId(47L);
		entity11.setVersion(2);
		entity11.setDrvierPhone("15000811122");
		entity11.setDrvierName("15000811122");
		entity11.setUseCarUnit("55");
		entity11.setBuyCarUnit("55");
		entity11.setUpdatedBy("admin");
		drvierService.updateDrvier(entity11);
	}

}
