package com.kuznetsov.linoleumShopRest.integration;

import com.kuznetsov.linoleumShopRest.annotation.IntegrationTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

@IntegrationTest
@Testcontainers
public abstract class IntegrationTestConfig {

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres:13");

    @DynamicPropertySource
    static void setJdbcUrlInProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",postgresqlContainer::getJdbcUrl);
    }
}
