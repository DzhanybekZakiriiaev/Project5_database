package com.example.basedatabaseaccessapplication;

import com.example.softwaredesigntechniques.BaseDatabaseAccessApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        classes = BaseDatabaseAccessApplication.class,
        properties = "spring.profiles.active=test"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConstraintsTest {

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void insertingReportWithoutExistingLog_shouldFail() {
        String sql = "INSERT INTO \"Reports\"(id, report_text, log_id, needs_repair) " +
                "VALUES (9999, 'bad fk', 424242, false)";
        assertThrows(Exception.class, () -> jdbc.update(sql));
    }
}
