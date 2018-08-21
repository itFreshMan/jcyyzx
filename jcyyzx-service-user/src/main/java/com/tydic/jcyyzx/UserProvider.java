package com.tydic.jcyyzx;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;

@EnableAsync
@EnableCaching
@EnableScheduling
@EnableDubboConfiguration
@SpringBootApplication
public class UserProvider extends SpringBootServletInitializer implements WebApplicationInitializer {

  public static void main(String... args) {
    SpringApplication.run(UserProvider.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    // Customize the application or call application.sources(...) to add sources
    // Since our example is itself a @Configuration class we actually don't
    // need to override this method.
    return application.sources(UserProvider.class);
  }

}
