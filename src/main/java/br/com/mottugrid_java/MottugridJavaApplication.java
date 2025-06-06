package br.com.mottugrid_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MottugridJavaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MottugridJavaApplication.class, args);
	}
}