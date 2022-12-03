package co.edu.icesi.VirtualStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class VirtualStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualStoreApplication.class, args);
	}
}
