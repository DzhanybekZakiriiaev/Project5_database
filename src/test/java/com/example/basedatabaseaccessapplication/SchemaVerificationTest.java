package com.example.basedatabaseaccessapplication;

import com.example.softwaredesigntechniques.BaseDatabaseAccessApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = BaseDatabaseAccessApplication.class,
        properties = "spring.profiles.active=test"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SchemaVerificationTest {

    @Autowired
    JdbcTemplate jdbc;

    private boolean tableExists(String schema, String tableExactCase) {
        final String sql =
                "SELECT EXISTS (" +
                        "  SELECT 1 FROM information_schema.tables " +
                        "  WHERE table_schema = ? AND (table_name = ? OR table_name = lower(?))" +
                        ")";
        return jdbc.queryForObject(sql, Boolean.class,
                (schema == null ? "public" : schema), tableExactCase, tableExactCase);
    }

    @Test
    void facilitiesTablesExist() {
        assertTrue(tableExists("public", "Machines"), "Table public.Machines should exist");
        assertTrue(tableExists("public", "Parts"),    "Table public.Parts should exist");
        assertTrue(tableExists("public", "Logs"),     "Table public.Logs should exist");
        assertTrue(tableExists("public", "Reports"),  "Table public.Reports should exist");
    }

    @Test
    void inventorySchemaIfPresent_hasCoreTables() {
        boolean items       = tableExists("inventory", "Items");
        boolean stockLevels = tableExists("inventory", "Stock_Levels");
        boolean stockLedger = tableExists("inventory", "Stock_Ledger");

        // Only enforce Items if any inventory table exists
        if (items || stockLevels || stockLedger) {
            assertTrue(items, "inventory.Items should exist when inventory schema is present");
        }
    }
}
