package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.persistence.EntityNotFoundException;

import static com.example.demo.ApplicationTests.ContainerInitializer;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(initializers = {ContainerInitializer.class})
class ApplicationTests {

	@Container
	private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer();

	@Autowired
	private TodoRepository repository;

	@Test
	void test() {
		Todo todo = repository.findById(1L).orElseThrow(EntityNotFoundException::new);
		assertThat(todo.getId()).isEqualTo(1L);
		assertThat(todo.getTitle()).isEqualTo("Build test environment.");
		assertThat(todo.isCompleted()).isFalse();
	}

	static class ContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext context) {
			MY_SQL_CONTAINER.start();
			TestPropertyValues.of(
					"spring.datasource.url=" + MY_SQL_CONTAINER.getJdbcUrl(),
					"spring.datasource.username=" + MY_SQL_CONTAINER.getUsername(),
					"spring.datasource.password=" + MY_SQL_CONTAINER.getPassword(),
					"spring.datasource.driver-class-name=" + MY_SQL_CONTAINER.getDriverClassName()
			).applyTo(context.getEnvironment());
		}
	}
}
