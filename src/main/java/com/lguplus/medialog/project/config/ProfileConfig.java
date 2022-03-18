package com.lguplus.medialog.project.config;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
public class ProfileConfig {

	/**
	 * Map을 캐시 저장소로 사용하는 경우
	 */
	@Configuration
	@Profile("local")
	public static class LocalConfig {
		@Bean
		public CacheManager cacheManager() {
			return new ConcurrentMapCacheManager("ui:cache:lnb", "ui:cache:menu");
		}
	}

	/**
	 * redis를 캐시 저장소로 사용하는 경우
	 */
	@Configuration
	@Profile("!local")
	public static class ProductionConfig implements BeanClassLoaderAware {
		private ClassLoader loader;

		public void setBeanClassLoader(ClassLoader classLoader) {
			this.loader = classLoader;
		}
		
//		// 이하 redis session, redis cache 설정
//		/**
//		 * spring-session이 redis에 데이터 저장 시 json으로 저장한다.
//		 * bean 이름이 springSessionDefaultRedisSerializer여야 한다.
//		 * https://rusyasoft.github.io/spring-security/2019/01/14/spring-security-session-redis-json/
//		 */
//		@Bean
//		public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//			// redis에 value를 json string으로 저장.
//			// 기본 설정인 JdkSerializationRedisSerializer가 적용되면 byte[]로 저장된다.
//			ObjectMapper om = new ObjectMapper();
//			om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
//			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Json 포멧 강제화 해제 (Unrecognized field 처리)
//			om.registerModules(SecurityJackson2Modules.getModules(loader)); // spring-security 객체 serialize/deserialize 지원
//
//			return new GenericJackson2JsonRedisSerializer(om);
//		}
//
//		@Bean
//		public RedisCacheConfiguration cacheConfiguration() {
//			// spring-cache가 redis에 데이터 저장 시 json으로 저장한다.
//			// https://www.concretepage.com/spring-boot/spring-boot-redis-cache
//			RedisSerializationContext.SerializationPair<Object> jsonSerializer =
//					RedisSerializationContext.SerializationPair.fromSerializer(springSessionDefaultRedisSerializer());
//
//			RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//					.entryTtl(Duration.ofSeconds(600))
//					.disableCachingNullValues()
//					.serializeValuesWith(jsonSerializer);
//
//			return cacheConfig;
//		}
//
//		@Bean
//		public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//			RedisCacheManager rcm = RedisCacheManager.builder(connectionFactory)
//					.cacheDefaults(cacheConfiguration())
//					.transactionAware()
//					.build();
//			return rcm;
//		}
	}

}
