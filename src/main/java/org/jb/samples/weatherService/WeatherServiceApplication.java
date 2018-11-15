package org.jb.samples.weatherService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class WeatherServiceApplication {
	
	public static void main(String[] args) {
        SpringApplication.run(WeatherServiceApplication.class, args);
    }

}
