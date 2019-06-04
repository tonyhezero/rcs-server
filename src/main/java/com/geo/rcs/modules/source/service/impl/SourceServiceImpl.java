package com.geo.rcs.modules.source.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.RcsCache;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.log.LogFileName;
import com.geo.rcs.common.okhttp.okHttpClient;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.engine.entity.*;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.source.dataSourceList.service.SeparationService;
import com.geo.rcs.modules.source.handler.SourceFactory;
import com.geo.rcs.modules.source.handler.ValidateHandler;
import com.geo.rcs.modules.source.service.DataSourceLogService;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.source.service.SourceMapperService;
import com.geo.rcs.modules.source.service.SourceService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;


/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.source.service.impl
 * @Description : RuleEngineer
 * @Author yongmingz
 * @email yongmingz@geotmt.com
 * @Creation Date : 2018.1.11
 */

@Service
public class SourceServiceImpl implements SourceService {
	private final Logger errorLogger = LogUtil.logger(LogFileName.API_ERROR_LOG);
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutorService sourceThreadPool;

	/**
	 * 数据平台地址
	 */
	@Value("${dataSourceServer.rcsDataSource.url}")
	private String dataSourceServerUrl;

	@Autowired
	SourceMapperService sourceMapperService;

	@Autowired
	InterfaceService interfaceService;
	@Autowired
	RedisTemplate redisTemplate;
	@Autowired
	SeparationService separationService;
	public final String getFieldRes() {
		return dataSourceServerUrl + "/dataSource/getFieldRes";
	}

	private Map<String, List<String>> systemValidInterMap = new HashMap<>();

	@PostConstruct
	void init(){
		sourceThreadPool = new ThreadPoolExecutor(200, 200,
				30L, TimeUnit.MINUTES,
				new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat("source-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());
	}

	@Override
	public String getFieldRes(String rulesConfig, Long userId, String channel) throws Exception {

		long startTime = System.currentTimeMillis();
		System.out.println("[RCS-INFO]:数据源引擎启动运行！");

		rulesConfig = getRulesSourceData(rulesConfig, userId, channel);

		long costTime = System.currentTimeMillis() - startTime;

		System.out.println("[RCS-INFO]:数据源引擎结束运行！costTime：" + costTime);

		return rulesConfig;
	}


	/**
	 * 调用三方数据源微服务模块，补充规则引擎输入数据
	 *
	 * @param
	 */

	@Override
	public String getFieldResByThird(String rulesConfig, Long userId) throws Exception {

		Map<String, Object> headersForm = new HashMap<>();
		Map<String, Object> postForm = new HashMap<>();
		postForm.put("rulesConfig", rulesConfig);
		postForm.put("userId", userId);

		if (dataSourceServerUrl == null || dataSourceServerUrl.length() == 0) {
			throw new RcsException(StatusCode.DATASERVER_CONFIG_ERROR.getMessage(), StatusCode.DATASERVER_CONFIG_ERROR.getCode());
		}
		try {
			return new okHttpClient().postByJSON(getFieldRes(),
					JSONObject.toJSONString(postForm),
					JSONObject.toJSONString(headersForm));
		} catch (ConnectException e) {
			throw new RcsException(StatusCode.DATASOURCE_CONN_ERROR.getMessage(), StatusCode.DATASOURCE_CONN_ERROR.getCode());
		} catch (SocketTimeoutException e) {
			throw new RcsException(StatusCode.DATASOURCE_CONN_TIMEOUT.getMessage(), StatusCode.DATASOURCE_CONN_TIMEOUT.getCode());
		} catch (Exception e) {
			throw new RcsException(StatusCode.DATASOURCE_ERROR.getMessage(), StatusCode.DATASOURCE_ERROR.getCode());
		}

	}

	/**
	 * 调用三方数据源微服务模块，请求原始接口数据
	 *
	 * @param
	 */

	@Override
	public String getInterResByThird(Long userId, List<String> interList, Map<String, String> parameters) throws Exception {

		Map<String, Object> headersForm = new HashMap<String, Object>();
		Map<String, Object> postForm = new HashMap<String, Object>();

		postForm.put("userId", userId.toString());
		postForm.put("interList", interList);
		postForm.put("params", parameters);

		if (dataSourceServerUrl == null || dataSourceServerUrl.length() == 0) {
			throw new RcsException(StatusCode.DATASERVER_CONFIG_ERROR.getMessage(), StatusCode.DATASERVER_CONFIG_ERROR.getCode());
		}

		return new okHttpClient().postByJSON(getFieldRes(),
				JSONObject.toJSONString(postForm),
				JSONObject.toJSONString(headersForm));
	}

	/**
	 * 调用三方数据源微服务模块，请求原始接口数据
	 *
	 * @param
	 */

	@Override
	public String getInterResByThird(Long userId, String innerIfType, Map<String, Object> parameters) {

		List<String> interList = new ArrayList<String>();
		interList.add(innerIfType);

		Map<String, Object> headersForm = new HashMap<String, Object>();
		Map<String, Object> postForm = new HashMap<String, Object>();

		postForm.put("interList", JSONObject.toJSONString(interList));
		postForm.put("params", JSONObject.toJSONString(parameters));
		postForm.put("userId", userId.toString());

		if (dataSourceServerUrl == null || dataSourceServerUrl.length() == 0) {
			throw new RcsException(StatusCode.DATASERVER_CONFIG_ERROR.getMessage(), StatusCode.DATASERVER_CONFIG_ERROR.getCode());
		}

		try {
			return new okHttpClient().postByJSON(getFieldRes(),
					JSONObject.toJSONString(postForm),
					JSONObject.toJSONString(headersForm));
		} catch (Exception e) {
			e.printStackTrace();
			return JSONObject.toJSONString(Geo.error(StatusCode.DATASOURCE_CONN_ERROR.getCode(), StatusCode.DATASOURCE_CONN_ERROR.getMessage()));
		}

	}

	private Map<String, List<Field>> getRulesFields(Rules rules){
		List<Rule> ruleSet = rules.getRuleList();
		Map<String, List<Field>> map = new HashMap<>();
		if(rules.getRuleList()==null){
			return map;
		}
		for (Rule rule:ruleSet) {
			if(rule.getConditionsList()==null){
				continue;
			}
			List<Condition> conSet = rule.getConditionsList();
			for (Condition con: conSet) {
				if(con.getFieldList()==null){
					continue;
				}
				List<Field> fieldList = con.getFieldList();
				for (Field field:fieldList) {
					map.computeIfAbsent(field.getFieldId(), k -> new ArrayList<>());
					map.get(field.getFieldId()).add(field);
					if(field.getFunctionSet()!=null){
						for (FunctionField functionField : field.getFunctionSet()) {
							map.computeIfAbsent(functionField.getFieldId(), k -> new ArrayList<>());
							map.get(functionField.getFieldId()).add(functionField);
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 从数据源补充字段数据到RulesConfig
	 *
	 * @return 规则集完整信息json
	 */
	private String getRulesSourceData(String rulesConfig, Long userId, String channel) throws Exception {
		Rules rules = JSONObject.parseObject(rulesConfig,Rules.class);
		Map<String, List<Field>> rulesFieldsMap = getRulesFields(rules);
		String parameters = JSONObject.toJSONString(rules.getParameters());
		HashMap<String,String> interFieldMap = new HashMap<>();
		Map map = new HashMap<>();
		if(!redisTemplate.hasKey(RcsCache.SOURCE_INTER_CACHE.getHeader()+userId)){
			List<Map<String, Object>> rulesFieList = sourceMapperService.findEntryMapByFieldId(userId);
			for (Map<String, Object> stringObjectMap : rulesFieList) {
				map.put(stringObjectMap.get("id").toString(),stringObjectMap);
			}
			redisTemplate.opsForHash().putAll(RcsCache.SOURCE_INTER_CACHE.getHeader()+userId,map);
			redisTemplate.expire( RcsCache.SOURCE_INTER_CACHE.getHeader()+userId,1,TimeUnit.DAYS);
		} else {
			List<Map> list = redisTemplate.opsForHash().multiGet(RcsCache.SOURCE_INTER_CACHE.getHeader()+userId,rulesFieldsMap.keySet());
			for (Map map1 : list) {
				map.put(map1.get("id").toString(),map1);
			}
		}
		Map<String,List<String>> interMap = new HashMap<>(6);
		Map<String,String> interMatchType = new HashMap<>();
		Set<String> interSet = new HashSet<>();
		for (String s : rulesFieldsMap.keySet()) {
			Map engineField = (Map) map.get(s);
			if(engineField == null){
				throw new RcsException(StatusCode.DATA_SOURCE_ERROR.getMessage(), StatusCode.DATA_SOURCE_ERROR.getCode());
			}
			interFieldMap.put((String)engineField.get("name"),(String)engineField.get("fieldName"));
			for (Field field : rulesFieldsMap.get(s)) {
				field.setFieldType((String) engineField.get("field_type"));
				field.setShowName((String) engineField.get("describ"));
				field.setFieldName((String) engineField.get("fieldName"));
			}
			if ("0".equals(engineField.get("active").toString())) {
				throw new RcsException(StatusCode.INTER_NOTFOUND_ERROR.getMessage(), StatusCode.INTER_NOTFOUND_ERROR.getCode());
			}
			if(engineField.get("name")==null){
				continue;
			}
			String interName = ValidateHandler.interNameValidate((String) engineField.get("name"));
			String dataSourceType = (String) engineField.get("request_type");
			if(!interSet.add(interName)){
				continue;
			}
			System.out.println(interName + "==请求类型:" + dataSourceType);
			interMatchType.put(interName,dataSourceType);
			interMap.computeIfAbsent(dataSourceType, k -> new ArrayList<String>());
			interMap.get(dataSourceType).add(interName);
		}

		/* 创建数据字典 */
		Map<String, Map> rulesData = new ConcurrentHashMap<>();

		CountDownLatch countDownLatch = new CountDownLatch(interMap.keySet().size());
		getSourceData(interMap,parameters,rulesData,countDownLatch, userId, channel,interFieldMap,rulesConfig);

		String rulesDataStr = JSON.toJSONString(rulesData);

		System.out.println("[RCS-INFO]:数据源调用外部返回结果：");
		System.out.println(rulesDataStr);

		/* 解析接口数据 */
		Map<String,String> parseResult;
		parseResult = SourceFactory.rulesDataPackager(rules, rulesData,interMatchType);
		rulesConfig = parseResult.get("rulesConfig");

		System.out.println("[RCS-INFO]:数据源引擎返回结果：");
		System.out.println(rulesConfig);
		return rulesConfig;
	}

	private void getSourceData(Map<String,List<String>> interMap,String parameters,Map<String, Map> rulesData,CountDownLatch countDownLatch, Long userId, String channel,Map<String,String> interFieldMap,String rulesConfig)  {
		final RcsException[] ex = new RcsException[1];
		List<String> engineInters = separationService.selectDataSourcePerm(userId);
		if(null==engineInters||engineInters.size()<=0) {
			throw new RcsException(StatusCode.DATA_SOURCE_ERROR.getMessage(), StatusCode.DATA_SOURCE_ERROR.getCode());

		}else{
			for (String sourceType : interMap.keySet()) {
					sourceThreadPool.execute(() -> {
						System.out.println("正在执行task ");
						Map<String, Map> mapData;
						try {
							List<String> strings = interMap.get(sourceType);
							for (String str:strings){
								if(engineInters.contains(str)||engineInters.contains(str.toLowerCase())){
									mapData=getResult(sourceType,interMap, parameters,rulesData,countDownLatch,userId,channel, interFieldMap, rulesConfig);
									rulesData.putAll(mapData);
								}else{
									throw new RcsException(StatusCode.DATA_SOURCE_ERROR.getMessage(), StatusCode.DATA_SOURCE_ERROR.getCode());
								}
							}


						} catch (RcsException e) {
							ex[0] = e;
							errorLogger.error("【数据源】获取数据源异常", e);
						}
						System.out.println("task 执行完毕");
						countDownLatch.countDown();
					});


			}
		}
		try {
			countDownLatch.await();
			if (ex[0] != null) {
				throw ex[0];
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取所有合法接口
	 *
	 * @return
	 */
	Map<String, List<String>> getInterMap() {

		List<EngineInter> interMap = interfaceService.getAllInter();
		for (EngineInter engineInter : interMap) {
			if (systemValidInterMap.get(engineInter.getRequestType()) == null) {
				List<String> list = new ArrayList<>();
				systemValidInterMap.put(engineInter.getRequestType(),
						list);
				systemValidInterMap.get(engineInter.getRequestType()).add(engineInter.getName());
			} else {
				systemValidInterMap.get(engineInter.getRequestType()).add(engineInter.getName());
			}
		}
		return systemValidInterMap;
	}
	private  Map<String,Map> getResult(String sourceType, Map<String,List<String>> interMap,String parameters,Map<String, Map> rulesData,CountDownLatch countDownLatch, Long userId, String channel,Map<String,String> interFieldMap,String rulesConfig) throws RcsException {
		Map<String, Map> mapData;
		switch (sourceType) {
			case ValidateHandler.PARAMETER_CALC:
				mapData = interfaceService.getParameterCalcData(interMap.get(sourceType), parameters, interFieldMap, rulesConfig, channel, userId);
				break;
			default:
				throw new RcsException(StatusCode.PARAMS_ERROR.getMessage(), StatusCode.PARAMS_ERROR.getCode());
		}
		return mapData;
	}
}
