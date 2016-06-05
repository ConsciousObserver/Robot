package test.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"test.*"})
public class TestRobotApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(TestRobotApplication.class, args);
	}
}
