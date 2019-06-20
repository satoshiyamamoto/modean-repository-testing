package com.example.demo;

import static com.example.demo.ApplicationTests.MySQLContainerInitializer;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@ContextConfiguration(initializers = {MySQLContainerInitializer.class})
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

  static class MySQLContainerInitializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      MY_SQL_CONTAINER.start();
      TestPropertyValues.of(
          "spring.datasource.url=" + MY_SQL_CONTAINER.getJdbcUrl(),
          "spring.datasource.username=" + MY_SQL_CONTAINER.getUsername(),
          "spring.datasource.password=" + MY_SQL_CONTAINER.getPassword(),
          "spring.datasource.driver-class-name=" + MY_SQL_CONTAINER.getDriverClassName(),
          "spring.jpa.show-sql=true",
          "spring.jpa.properties.hibernate.format_sql=true",
          "spring.flyway.enabled=true"
      ).applyTo(context.getEnvironment());
    }
  }
}
