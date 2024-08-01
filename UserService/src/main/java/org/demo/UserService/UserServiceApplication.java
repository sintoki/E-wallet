package org.demo.UserService;

import org.demo.UserService.Repository.UserRepository;
import org.demo.UserService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;



@SpringBootApplication(scanBasePackages = {"org.demo", "Service"})
public class UserServiceApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${service.authority}")
	private String serviceAuthority;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user= User.builder().
				name("txn-service")
				.password(passwordEncoder.encode("txn-service"))
				.email("txnservice@gmail.com")
				.authorities(serviceAuthority)
				.contact("txn-service")
				.build();
		userRepository.save(user);

	}
}
