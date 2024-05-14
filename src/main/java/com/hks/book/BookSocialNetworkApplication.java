package com.hks.book;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.hks.book.entity.Role;
import com.hks.book.repository.RoleRepo;

@SpringBootApplication()
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSocialNetworkApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepo roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()&&roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
				roleRepository.save(Role.builder().name("ADMIN").build());
			}
		};
	}
}
