package com.study.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
@Import(TestContainerConfig.class)
public class TestRedisApplication {

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry,
							  @Autowired PostgreSQLContainer<?> postgresContainer,
							  @Autowired @Qualifier("redis") GenericContainer<?> redisContainer) {
		dynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
		dynamicPropertyRegistry.add("spring.jpa.generate-ddl", () -> "true");
		dynamicPropertyRegistry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

		System.setProperty("spring.redis.host", redisContainer.getHost());
		System.setProperty("spring.redis.port", String.valueOf(redisContainer.getMappedPort(6379)));
	}

	public static void main(String[] args) {
		SpringApplication.from(RedisApplication::main).with(TestRedisApplication.class).run(args);
	}
}
