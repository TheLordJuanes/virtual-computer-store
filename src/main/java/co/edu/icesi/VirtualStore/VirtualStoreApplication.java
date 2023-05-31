package co.edu.icesi.VirtualStore;

import co.edu.icesi.VirtualStore.model.Role;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.repository.RoleRepository;
import co.edu.icesi.VirtualStore.repository.UserRepository;
import co.edu.icesi.VirtualStore.service.utils.Encoder;
import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import java.util.UUID;

@SpringBootApplication
@EnableJpaRepositories
public class VirtualStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualStoreApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository) {
		Role adminRole = Role.builder().id(UUID.fromString("44c953c9-daf4-45a9-bb41-288fce256c43"))
				.name("Admin")
				.description("The administrator user role.")
				.build();
		Role basicRole = Role.builder().id(UUID.fromString("c7cc9bab-62e1-4be1-98d3-8908a2c1784f"))
				.name("Basic")
				.description("The basic user role.")
				.build();
		User userAdmin = User.builder().id(UUID.fromString("724f2a12-cad4-4e65-8846-9dd94df19369"))
				.email("juanes.caicedo@icesi.edu.co")
				.password("Ju@nes1234")
				.address("Calle 45 #34-21")
				.phoneNumber("+573107552187")
				.role(adminRole).build();
		byte[] hashedBytes = Encoder.hashPassword(userAdmin.getPassword().toCharArray(), userAdmin.getId().toString().getBytes());
		String hashedString = Hex.encodeHexString(hashedBytes);
		userAdmin.setPassword(hashedString);
		User userBasic = User.builder().id(UUID.fromString("9db26d90-ff5c-477d-bc4b-95cae0363a71"))
				.email("juan.fernando@icesi.edu.co")
				.password("Fern@ndo1234")
				.address("Calle 87 #91-19")
				.phoneNumber("+573128492745")
				.role(basicRole).build();
		hashedBytes = Encoder.hashPassword(userBasic.getPassword().toCharArray(), userBasic.getId().toString().getBytes());
		hashedString = Hex.encodeHexString(hashedBytes);
		userBasic.setPassword(hashedString);

		return args -> {
			roleRepository.save(adminRole);
			roleRepository.save(basicRole);
			userRepository.save(userAdmin);
			userRepository.save(userBasic);
		};
	}
}
