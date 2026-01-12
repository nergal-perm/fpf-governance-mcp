package org.fpf.governance;

import org.neo4j.driver.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GovernanceApplication {

    private static final Logger logger = LoggerFactory.getLogger(GovernanceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GovernanceApplication.class, args);
    }

}
