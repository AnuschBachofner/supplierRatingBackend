package io.github.supplierratingsoftware.supplierratingbackend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for Cross-Origin Resource Sharing (CORS).
 *
 * <p>
 * This configuration is strictly limited to the `dev-local` profile to enable
 * communication with the local frontend during development.</p>
 * <p>
 * NOTE:
 * In production environments (Docker/Nginx), CORS is handled by the reverse proxy or
 * is not required at all due to same-origin serving.</p>
 */
@Configuration
@Profile("dev-local")
public class CorsConfig {

    private static final String FRONTEND_DEV_ORIGIN = "http://localhost:4200";
    private static final String API_PATH_PATTERN = "/**";
    private static final String ALLOWED_HEADERS = "*";

    /**
     * Creates a WebMvcConfigurer bean for configuring CORS.
     *
     * @return WebMvcConfigurer bean
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Configures CORS mappings for the specified API path pattern.
             *
             * @param registry CorsRegistry for configuring CORS mappings. This is provided by Spring.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(API_PATH_PATTERN)
                        .allowedOrigins(FRONTEND_DEV_ORIGIN)
                        .allowedMethods(
                                HttpMethod.GET.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.PUT.name(),
                                HttpMethod.OPTIONS.name()
                        )
                        .allowedHeaders(ALLOWED_HEADERS)
                        .allowCredentials(true);
            }
        };
    }
}

