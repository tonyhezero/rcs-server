package com.geo.rcs.modules.source.handler;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.CommonJsonParseUtil;
import com.geo.rcs.common.util.JSONUtil;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.geo.rcs.common.StatusCode.RES_ERROR;
import static com.geo.rcs.modules.source.entity.InterStatusCode.BASICDICT;
import static com.geo.rcs.modules.source.entity.InterStatusCode.ECLDICT;

public class ResponseParser {
	// 初始化常量
	private static final String CODE = "code";
	private static final String SUCCSEE_CODE = "200";
	private static final String DATA = "data";
	private static final String ISPNUM = "ISPNUM";
	private static final String RSL = "RSL";
	private static final String ECL = "ECL";
	private static final String IFT = "IFT";
	private static final String RS = "RS";
	private static final String DESC = "desc";
	private static final String INTER = "inter";
	private static final String FIELD = "field";
	private static final String VALUE = "value";
	private static final String VALUEDESC = "valueDesc";

	/**
	 * 主方法:解析集奥聚合返回数据
	 * 
	 * @param data
	 *            例：
	 *            "{\"code\":\"200\",\"data\":{\"ISPNUM\":{\"province\":\"湖北\",\"city\":\"宜昌\",\"isp\":\"联通\"
	 *            },
	 *            \"RSL\":[{\"RS\":{\"code\":\"3\",\"desc\":\"(24,+)\"},\"IFT\":\"A3\"},{\"RS\":{\"code\":\"002\",\"desc\":\"(40,80]\"
	 *            },
	 *            \"IFT\":\"B1\"},{\"RS\":{\"code\":\"0\",\"desc\":\"正常在用\"},\"IFT\":\"A4\"},{\"RS\":{\"code\":\"0\",\"desc\":\"三维验证一致\"
	 *            },
	 *            \"IFT\":\"B7\"},{\"RS\":{\"code\":\"1\",\"desc\":\"否\"},\"IFT\":\"B11\"},],\"ECL\":[{\"code\":\"10000002\",\"IFT\":\"B13\"
	 *            }]}, \"msg\":\"成功\"}"
	 * @param innerIfType
	 *            接口类型
	 * @return
	 */
	public static Map<String, Map> ParserBasic(String data, String innerIfType) {
		Map<String, Map> interData = new HashMap<>();

		if (data != null && data.length() != 0) {
			JSONObject response = JSONObject.parseObject(data);
			if (response.getString(CODE).equals(SUCCSEE_CODE)) {
				JSONObject basicInfo = response.getJSONObject(DATA).getJSONObject(ISPNUM);
				JSONArray interRslData = response.getJSONObject(DATA).getJSONArray(RSL);    
				JSONArray interEclData = response.getJSONObject(DATA).getJSONArray(ECL);

				/**
				 * 归属地信息字段数据
				 */
				if (basicInfo != null) {
					for (String key : basicInfo.keySet()) {
						Map<String, Object> _interData = new HashMap<>();
						_interData.put(INTER, "");
						_interData.put(FIELD, key);
						_interData.put(VALUE, basicInfo.getString(key));
						_interData.put(VALUEDESC, "");
						interData.put(key, _interData);
					}
				}

				/**
				 * 正常调用结果字段数据
				 */
				interData.putAll(paserInterData(interRslData, innerIfType));

				/**
				 * 异常结果字段数据
				 */
					if (ValidateHandler.INTER2FIELD.keySet().contains(innerIfType)) {
						for (int i = 0; i < interEclData.size(); i++) {
							Map<String, Object> _interData = new HashMap<>();
							JSONObject object = (JSONObject) interEclData.get(i);
							String inter = object.getString(IFT);
							String code = object.getString(CODE);

							// todo: 单接口字典表
							String field = ValidateHandler.INTER2FIELD.get(inter);
							_interData.put(INTER, inter);
							_interData.put(FIELD, field);
							_interData.put(VALUE, code);
							_interData.put(VALUEDESC, ECLDICT.get(code));
							interData.put(field, _interData);
						}
					} else if (ValidateHandler.MULTIINTER2FIELD.keySet().contains(innerIfType)) {
					for (int i = 0; i < interEclData.size(); i++) {
						JSONObject object = (JSONObject) interEclData.get(i);
						String inter = object.getString(IFT);
						String code = object.getString(CODE);

						// todo: 多接口字典表
						for (String field : ValidateHandler.MULTIINTER2FIELD.get(innerIfType)) {
							Map<String, Object> _interData = new HashMap<>();
							_interData.put(INTER, inter);
							_interData.put(FIELD, field);
							_interData.put(VALUE, code);
							_interData.put(VALUEDESC, ECLDICT.get(code));
							interData.put(field, _interData);
						}
					}
				}

			} else {
				String errorCode = response.getString("code");
				throw new RcsException(BASICDICT.get(errorCode), RES_ERROR.getCode());
			}
		} else {
			throw new RcsException(RES_ERROR.getMessage(), RES_ERROR.getCode());
		}

		return interData;
	}

	/**
	 * 子方法: 解析集奥风控详细接口数据
	 * 
	 * @param interRslData
	 * @param innerIfType
	 * @return
	 */
	public static Map<String, Map> paserInterData(JSONArray interRslData, String innerIfType) {
		Map<String, Map> interData = new HashMap<>();
		Map<String, List<String>> interfaceDic = ValidateHandler.getInterfaceDic();

		for (int i = 0; i < interRslData.size(); i++) {
			JSONObject object = (JSONObject) interRslData.get(i);
			String inter = object.getString(IFT);
			String code = object.getJSONObject(RS).getString(CODE);
			String desc = object.getJSONObject(RS).getString(DESC);

			if (interfaceDic.get(ValidateHandler.SINGLEFIELD_1).contains(innerIfType)) {
				// todo: 单接口字段表,"code": "0","desc": "(0,6]"
				Map<String, Object> _interData = new HashMap<>();
				_interData.put(INTER, inter);
				_interData.put(FIELD, ValidateHandler.INTER2FIELD.get(inter));
				_interData.put(VALUE, code);
				_interData.put(VALUEDESC, desc);
				interData.put(ValidateHandler.INTER2FIELD.get(inter), _interData);

			} else if (interfaceDic.get(ValidateHandler.SINGLEFIELD_2).contains(innerIfType)) {
				// todo: 单接口特殊字段表,c6 "code": "-9999","desc": "ZTE-NX511J" 2.3.30
				// C6-手机号终端型号查询
				Map<String, Object> _interData = new HashMap<>();
				_interData.put(INTER, inter);
				_interData.put(FIELD, ValidateHandler.INTER2FIELD.get(inter));
				_interData.put(VALUE, desc);
				_interData.put(VALUEDESC, "");
				interData.put(ValidateHandler.INTER2FIELD.get(inter), _interData);

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_1).contains(innerIfType)) {
				// todo: 多接口字段表1 Y1 姓名身份证号验证"code": "-9999","desc":
				// "{\"birthday\":\"19880502\",\"gender\":\"男\",\"originalAdress\":\"黑龙江省齐齐哈尔市克东县\",\"identityResult\":\"0\",\"age\":\"28\"}"

				Map<String, Object> descMap = JSONUtil.jsonToMap(desc);
				if (descMap != null && !descMap.isEmpty()) {
					for (String key : descMap.keySet()) {
						if(interfaceDic.get(ValidateHandler.MULTIFIELD_1_1).contains(innerIfType)){
							Map<String, Object> _interData = new HashMap<>();
							_interData.put(INTER, inter);
							_interData.put(FIELD, "illegal_"+key);
							_interData.put(VALUE, descMap.get(key));
							_interData.put(VALUEDESC, "");
							interData.put("illegal_"+key, _interData);
						}else {
							Map<String, Object> _interData = new HashMap<>();
							_interData.put(INTER, inter);
							_interData.put(FIELD, key);
							_interData.put(VALUE, descMap.get(key));
							_interData.put(VALUEDESC, "");
							interData.put(key, _interData);
						}
						System.out.println(key);
					}
				}

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_5).contains(innerIfType)) {
				// todo: 多接口字段表5 Y1 评分模型  解析字段 = 接口_+结果字段
				// "{\"birthday\":\"19880502\",\"gender\":\"男\",\"originalAdress\":\"黑龙江省齐齐哈尔市克东县\",\"identityResult\":\"0\",\"age\":\"28\"}"

				Map<String, Object> descMap = JSONUtil.jsonToMap(desc);
				if (descMap != null && !descMap.isEmpty()) {
					for (String key : descMap.keySet()) {
						Map<String, Object> _interData = new HashMap<>();
						_interData.put(INTER, inter);
						_interData.put(FIELD, inter+"_"+key);
						_interData.put(VALUE, descMap.get(key));
						_interData.put(VALUEDESC, "");
						interData.put(inter+"_"+key, _interData);
						System.out.println(inter+"_"+key);
					}
				}

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_2).contains(innerIfType)) {
				// todo: 多接口特殊字段表2 Z7 银行卡钱包位置查询 "code": "-9999","desc":
				// "{\"error_code\":0,\"data\":[{\"account_no\":\"6228483738174751273\",\"CSSP001\":\"3\",\"score\":null}]}"

				Map<String, Object> descMap = JSONUtil.jsonToMap(desc);
				List<Map<String, Object>> dataList = JSONUtil.jsonToBean(JSONUtil.beanToJson(descMap.get(DATA)),
						List.class);
				for (Map<String, Object> map : dataList) {
					if (map != null && !map.isEmpty()) {
						for (String key : map.keySet()) {
							Map<String, Object> _interData = new HashMap<>();
							_interData.put(INTER, inter);
							_interData.put(FIELD, key);
							_interData.put(VALUE, map.get(key));
							_interData.put(VALUEDESC, "");
							interData.put(key, _interData);
							System.out.println(key);

						}
					}
				}

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_3).contains(innerIfType)) {
				// todo: 多接口多维数据字段表3,
				Map<String, Object> descMap = JSONUtil.beanToMap(desc);
				for (String key : descMap.keySet()) {
					Object obj = descMap.get(key);
					if (obj instanceof List) {
						List<Map<String, Object>> innerList = (List<Map<String, Object>>) obj;
						for (int j = 0; j < innerList.size(); j++) {
							Map<String, Object> innerMap = innerList.get(j);
							for (String innerKey : innerMap.keySet()) {
								Map<String, Object> _interData = new HashMap<>();
								String field = key + "_" + j + "_" + innerKey;
								_interData.put(INTER, inter);
								_interData.put(FIELD, field);
								_interData.put(VALUE, innerMap.get(innerKey));
								_interData.put(VALUEDESC, "");
								interData.put(field, _interData);
								System.out.println(field);
							}
						}
					} else if (obj instanceof Map) {
						Map<String, Object> map = (Map<String, Object>) obj;
						for (String innerKey : map.keySet()) {
							Map<String, Object> _interData = new HashMap<>();
							String field = key + "_" + innerKey;
							_interData.put(INTER, inter);
							_interData.put(FIELD, field);
							_interData.put(VALUE, map.get(innerKey));
							_interData.put(VALUEDESC, "");
							interData.put(field, _interData);
							System.out.println(field);

						}
					} else if (obj instanceof String) {
						Map<String, Object> _interData = new HashMap<>();
						_interData.put(INTER, inter);
						_interData.put(FIELD, key);
						_interData.put(VALUE, obj);
						_interData.put(VALUEDESC, "");
						interData.put(key, _interData);
						System.out.println(key);

					}
				}
			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_4).contains(innerIfType)){
				// todo: 多接口多维数据字段表4
				JSONArray descArray = JSONArray.parseArray(desc);
				for (int j = 0; j < descArray.size(); j++) {
					String expandKey = Integer.toString(j);
					Map<String, Object> descMap = JSONUtil.jsonToMap(JSONObject.toJSONString(descArray.get(j)));
					if (descMap != null && !descMap.isEmpty()) {
						String _expandKey = descMap.get("dataType").toString();
						expandKey = (_expandKey != null && _expandKey.length() != 0) ? _expandKey : expandKey;

						for (String key : descMap.keySet()) {
							Map<String, Object> _interData = new HashMap<>();
							_interData.put(INTER, inter);
							_interData.put(FIELD, expandKey + "_" + key);
							_interData.put(VALUE, descMap.get(key).toString().replace("{",""));
							_interData.put(VALUEDESC, "");
							interData.put(expandKey + "_" + key, _interData);
							System.out.println(expandKey + "_" + key);
						}
					}
				}
			}else{
				try{
					if(!JSONUtil.isJson(desc)&&!JSONUtil.isJsonArray(desc)){
						Map<String, Object> interDataMap = new HashMap<>();
						interDataMap.put(INTER, inter);
						interDataMap.put(FIELD, CODE);
						interDataMap.put(VALUE, code);
						interDataMap.put(VALUEDESC, desc);
						interData.put(CODE, interDataMap);
					}else{
						if(JSONUtil.isJsonArray(desc)){
							List<Map<String,Object>> list = JSON.parseObject(desc, List.class);
							for(Map<String,Object> map:list){
								for(String str:map.keySet()){
									Map<String, Object> _interData = new HashMap<>();
									System.out.println(map);
									_interData.put(INTER, inter);
									_interData.put(FIELD, str);
									_interData.put(VALUE,map.get(str));
									_interData.put(VALUEDESC, map.get(str));
									interData.put(str, _interData);
								}
							}
						}else{
							Map<String, Object> parse = CommonJsonParseUtil.parse(desc);
							for(String str:parse.keySet()){
								Map<String, Object> _interData = new HashMap<>();
								System.out.println(parse);
								_interData.put(INTER, inter);
								_interData.put(FIELD, str);
								_interData.put(VALUE, parse.get(str));
								_interData.put(VALUEDESC, parse.get(str));
								interData.put(str, _interData);
							}
						}

					}
				}catch (Exception e){
					Map<String, Object> interDataMap = new HashMap<>();
					interDataMap.put(INTER, inter);
					interDataMap.put(FIELD, CODE);
					interDataMap.put(VALUE, code);
					interDataMap.put(VALUEDESC, desc);
					interData.put(CODE, interDataMap);
				}

			}
		}

//		if (ValidateHandler.MULTIINTER2FIELD.keySet().contains(innerIfType)) {
//			List<String> keyList = new ArrayList<>(interData.keySet());
//			for (int i = keyList.size() - 1; i >= 0; i--) {
//				if (!ValidateHandler.MULTIINTER2FIELD.get(innerIfType).contains(keyList.get(i))) {
//					interData.remove(keyList.get(i));
//				}
//			}
//		}

		String json = JSONUtils.toJSONString(interData);
        return interData;
	}

	/**
	 * 解析入参计算返回的数据
	 * @param inter
	 * @param data
	 * @return
	 */
	public static Map<String,Map> parserParameterCalcData(String inter,String data){

		Map<String,Object> map = JSONUtil.jsonToMap(data);
		Map<String,Map> result = new HashedMap();
		for(Map.Entry<String,Object> entry : map.entrySet()){
			Map<String, Object> _interData = new HashMap<>();
			_interData.put(INTER, inter);
			_interData.put(FIELD, entry.getKey());
			_interData.put(VALUE, entry.getValue());
			_interData.put(VALUEDESC, "");
			result.put(entry.getKey(), _interData);
		}
		return result;
	}

	public static Map<String,Map> parserDataPlateFormData(String data ){

		List<Map<String,Object>> list = JSONUtil.jsonToBean(data,List.class);
		Map<String,Map> result = new HashedMap();

		for (Object obj : list){
			String objJson = JSON.toJSONString(obj);
			Map<String,Object> map =  JSONUtil.jsonToMap(objJson);
			Map<String, Object> _interData = new HashMap<>();
			_interData.put(INTER,map.get("indexName"));
			_interData.put(FIELD, map.get("indexName"));
			_interData.put(VALUE, map.get("num"));
			_interData.put(VALUEDESC, "");
			result.put((String)map.get("indexName"),_interData);
		}
		return result;
	}


}
