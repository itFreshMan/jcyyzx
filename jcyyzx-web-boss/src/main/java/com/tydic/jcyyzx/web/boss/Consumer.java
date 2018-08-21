package com.tydic.jcyyzx.web.boss;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@EnableDubboConfiguration
@SpringBootApplication
public class Consumer extends SpringBootServletInitializer implements WebApplicationInitializer {
	
	public static void main(String... args) {
		SpringApplication.run(Consumer.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// Customize the application or call application.sources(...) to add sources
		// Since our example is itself a @Configuration class we actually don't
		// need to override this method.
		return application.sources(Consumer.class);
	}
	
}
