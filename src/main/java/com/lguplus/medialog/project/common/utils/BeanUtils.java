package com.lguplus.medialog.project.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class BeanUtils {
	private static final ThreadLocal<ObjectMapper> mapperPerThread = new ThreadLocal<ObjectMapper>() {
		@Override
		protected ObjectMapper initialValue() {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			// java8 LocalDateTime 사용 (필요시 jackson-datatype-jsr310 dependency 추가)
			objectMapper.registerModule(new JavaTimeModule()); // 또는 objectMapper.findAndRegisterModules()
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			
			return objectMapper;
		}
	};

	public static ObjectMapper getObjectMapper() {
		return mapperPerThread.get();
	}

	public static <T> T map2bean(Map<String, ?> map, Class<T> clazz) {
		return getObjectMapper().convertValue(map, clazz);
	}

	public static Map<String, Object> bean2map(Object bean) {
//		return getObjectMapper().convertValue(bean, Map.class);
		return getObjectMapper().convertValue(bean, new TypeReference<Map<String, Object>>() {});
	}

	public static <T> Map<String, T> bean2map(Object bean, @SuppressWarnings("unused") Class<T> valueType) {
		return getObjectMapper().convertValue(bean, new TypeReference<Map<String, T>>() {});
	}
	
	public static <T> T json2bean(String json, Class<T> clazz) {
		if (json == null)
			return null;
		try {
			if (json.isEmpty())
				json = "{}";
			return getObjectMapper().readValue(json, clazz);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String bean2json(Object bean) {
		try {
			return getObjectMapper().writeValueAsString(bean);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String map2json(Map<String, ?> map) {
		try {
			return getObjectMapper().writeValueAsString(map);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2map(String json) {
		return json2bean(json, Map.class);
	}

	@SuppressWarnings({"unchecked", "unused"})
	public static <T> Map<String, T> json2map(String json, Class<T> valueType) {
		return json2bean(json, Map.class);
	}

	public static Map<String, Object> toObjectMap(Map<String, ?> map) {
		return new HashMap<String, Object>(map);
	}
}
