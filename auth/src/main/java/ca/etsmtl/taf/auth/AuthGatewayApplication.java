package ca.etsmtl.taf.auth;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import ca.etsmtl.taf.auth.repository.RoleRepository;
import ca.etsmtl.taf.auth.entity.ERole;
import ca.etsmtl.taf.auth.entity.Role;
import java.util.Optional;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@Log4j2
public class AuthGatewayApplication implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthGatewayApplication.class, args);
	}


	 @Override
    public void run(String... args) {
        System.out.println("Running startup script...");
        this.createRoleIfNotFound(ERole.ROLE_ADMIN);
        this.createRoleIfNotFound(ERole.ROLE_USER);
    }

	private void createRoleIfNotFound(ERole eRole){
        Optional<Role> userRoleExist = roleRepository.findByName(eRole);
        if(userRoleExist.isEmpty()){
            Role toSave = new Role(ERole.ROLE_USER);
            roleRepository.save(toSave);
        }
	}

}