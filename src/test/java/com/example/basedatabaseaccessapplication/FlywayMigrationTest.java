package com.example.basedatabaseaccessapplication;

import com.example.softwaredesigntechniques.BaseDatabaseAccessApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = BaseDatabaseAccessApplication.class,
        properties = "spring.profiles.active=test"
)
class FlywayMigrationTest {
    @Test
    void contextLoads_andFlywayRuns() {
        // starting the context = Flyway ran
    }
}