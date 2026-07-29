package com.company.golinks;

import com.company.golinks.model.Shortcut;
import com.company.golinks.repository.ShortcutRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GoLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoLinkApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(ShortcutRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Shortcut("design-system", "https://figma.com/@design-system", "Company Figma Design Tokens"));
                repository.save(new Shortcut("oncall", "https://pagerduty.com/oncall", "Current Engineering On-Call Schedule"));
                repository.save(new Shortcut("payroll", "https://gusto.com/login", "HR & Payroll Portal"));
            }
        };
    }
}
