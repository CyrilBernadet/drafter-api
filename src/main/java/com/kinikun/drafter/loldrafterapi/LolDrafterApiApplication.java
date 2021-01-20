package com.kinikun.drafter.loldrafterapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class LolDrafterApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LolDrafterApiApplication.class, args);
	}

}
