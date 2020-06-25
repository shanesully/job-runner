package com.workday.techtest;

import com.workday.techtest.scenarios.ExampleScenario;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

		ExampleScenario.run();
	}
}
