package com.digitalfir;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.digitalfir.backend.model.Role;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.UserRepository;

@SpringBootApplication
public class DigitalFir1Application {

	public static void main(String[] args) {
		SpringApplication.run(DigitalFir1Application.class, args);
		}
		
		
	@Bean
	CommandLineRunner createAdmin(UserRepository repo, PasswordEncoder encoder) {
	    return args -> {
	        if (!repo.existsByEmail("admin@digitalfir.com")) {

	            User admin = new User();
	            admin.setName("System Admin");
	            admin.setEmail("admin@digitalfir.com");
	            admin.setPassword(encoder.encode("admin123"));
	            admin.setRole(Role.ADMIN);
	            admin.setEnabled(true);

	            repo.save(admin);

	            System.out.println("✅ ADMIN user created");
	        }
	    };
	}


}
