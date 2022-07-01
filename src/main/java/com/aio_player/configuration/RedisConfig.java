package com.aio_player.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private String host = "localhost";
	//private String host = "172.105.50.222";
	private int port = 6379;

	@Bean
	public JedisClientConfiguration poolConfig() {
		final JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.defaultConfiguration();
		GenericObjectPoolConfig<?> genericObjectPoolConfig = jedisClientConfiguration.getPoolConfig().get();
		genericObjectPoolConfig.setMaxTotal(16);
		genericObjectPoolConfig.setMaxIdle(10);
		genericObjectPoolConfig.setMinIdle(5);
		genericObjectPoolConfig.setMaxWaitMillis(3000);
		return jedisClientConfiguration;
	}

	@Bean
	public JedisConnectionFactory getConnection() {
		final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName(host);
		configuration.setPort(port);
		// configuration.setPassword("root");
		return new JedisConnectionFactory(configuration, poolConfig());
	}

	@Bean
	public RedisTemplate<String, Object> getTempLate() {
		Jackson2JsonRedisSerializer jrs = new Jackson2JsonRedisSerializer(String.class);
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(getConnection());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		//template.setHashValueSerializer(new JdkSerializationRedisSerializer());
		// template.setHashValueSerializer(jrs);

		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();
		return template;
	}
}
