package com.example.runtime_config;

import com.example.runtime_config.config.TestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
	TestConfig.class
})
public class RuntimeConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(RuntimeConfigApplication.class, args);
	}

}
