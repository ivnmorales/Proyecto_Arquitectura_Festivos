package calendario.api.presentacion.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI calendarioOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Calendario")
                        .version("1.0.0")
                        .description("API REST para generar y consultar el calendario laboral "
                                + "(PostgreSQL), usando los festivos calculados por la API Festivos."));
    }
}
