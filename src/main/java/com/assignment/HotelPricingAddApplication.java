package com.assignment;

import com.assignment.agent.AddArchitectAgent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class HotelPricingAddApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelPricingAddApplication.class, args);
    }

    @Bean
    @Profile("!extract")
    public CommandLineRunner run(AddArchitectAgent agent) {
        return args -> agent.runAllIterations();
    }
}
