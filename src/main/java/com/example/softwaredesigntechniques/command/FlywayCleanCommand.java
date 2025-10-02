package com.example.softwaredesigntechniques.command;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FlywayCleanCommand implements CommandLineRunner {

    @Autowired
    private Flyway flyway;

    @Override
    public void run(String... args) throws Exception {
        // Only run this if the --clean-flyway argument is passed
        if (args.length > 0 && "--clean-flyway".equals(args[0])) {
            System.out.println("🧹 Cleaning Flyway schema history...");
            try {
                flyway.clean();
                System.out.println("✅ Flyway clean completed successfully!");
                System.out.println("🔄 Running migrations...");
                flyway.migrate();
                System.out.println("✅ Migrations completed successfully!");
            } catch (Exception e) {
                System.err.println("❌ Error during Flyway operations: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
