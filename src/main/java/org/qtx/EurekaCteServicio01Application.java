package org.qtx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableHystrix
@EnableEurekaClient 
@SpringBootApplication
@ServletComponentScan("org.qtx.web")
public class EurekaCteServicio01Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaCteServicio01Application.class, args);
	}

}
