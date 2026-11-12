package calendario.api.presentacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Escanea todos los módulos bajo calendario.api
@SpringBootApplication(scanBasePackages = "calendario.api")
@EnableJpaRepositories(basePackages = "calendario.api.infraestructura.persistencia.jpa")
@EntityScan(basePackages = "calendario.api.infraestructura.persistencia.entidades")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
