package com.balagan.balaganShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.balagan.balaganShop.repositories")
@EntityScan("com.balagan.balaganShop.models")
public class BalaganShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalaganShopApplication.class, args);
	}

}
