package com.geo.rcs.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON工具类
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/11/16 15:13
 */
public abstract class JSONUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final JsonFactory JSONFACTORY = new JsonFactory();

	public static String beanToJson(Object o) {
		StringWriter sw = new StringWriter(300);
		JsonGenerator gen = null;
		try {
			gen = JSONFACTORY.createGenerator(sw);
			MAPPER.writeValue(gen, o);

		} catch (Exception e) {

		} finally {
			if (gen != null) {
                try {
                    gen.close();
                } catch (IOException ignored) {
                }
            }
		}
		return sw.toString().replace("\\","")
							.replace("\"{","{")
							.replace("}\"","}");
	}

	public static <Value> Map<String, Value> beanToMap(Object o) {
		try {
			return ((Map) MAPPER.readValue(beanToJson(o), HashMap.class));
		} catch (IOException e) {
			throw new RuntimeException("转换失败", e);
		}
	}

	public static <Object> Map<String, Object> jsonToMap(String json) {
		try {
			return ((Map) MAPPER.readValue(json, HashMap.class));
		} catch (IOException e) {
			throw new RuntimeException("转换失败", e);
		}
	}

	public static <T> T jsonToBean(String json, Class<T> type) {
		if(json == null || json.trim() == ""){
			return null;
		}
		try {
			return MAPPER.readValue(json, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static <T> T mapToBean(Map map, Class<T> type) {
		try {
			return MAPPER.readValue(MAPPER.writeValueAsString(map),type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void validateJSON(String json) throws IOException {
		JsonParser parser = null;
		try {
			parser = JSONFACTORY.createParser(json);
			while (parser.nextToken() != null) {
            }
			if (null == parser) {
                return;
            }
			parser.close();
		} finally {
			if (null != parser) {
                parser.close();
            }
		}
	}

	static {
		MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		try {
			Class GString = Class.forName("groovy.lang.GString");
			SimpleModule gStringModule = new SimpleModule();
			gStringModule.addSerializer(GString, new JsonSerializer() {
				@Override
				public void serialize(Object value, JsonGenerator jgen,
									  SerializerProvider provider) throws IOException {
					jgen.writeString(String.valueOf(value));
				}
			});
			MAPPER.registerModule(gStringModule);
		} catch (Throwable ignored) {
		}
		MAPPER.getSerializationConfig().withSerializationInclusion(
				JsonInclude.Include.NON_NULL);
	}
	public static boolean isJson(String content) {
		try {
			JSONObject jsonStr = JSONObject.parseObject(content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isJsonArray(String content) {
		Object object = JSON.parse(content);
		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) object;
			return true;
		}
		return false;
	}
}
