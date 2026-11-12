package calendario.api.infraestructura.integracion;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import calendario.api.core.excepciones.ServicioExternoExcepcion;

/**
 * Encapsula las llamadas HTTP con RestTemplate.
 */
@Component
public class HttpServicio {

    private final RestTemplate restTemplate;

    public HttpServicio(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(15))
                .build();
    }

    public <T> T get(String url, Class<T> tipoRespuesta) {
        try {
            return restTemplate.getForObject(url, tipoRespuesta);
        } catch (RestClientException e) {
            throw new ServicioExternoExcepcion(
                    "No fue posible consumir el servicio externo: " + url, e);
        }
    }
}
