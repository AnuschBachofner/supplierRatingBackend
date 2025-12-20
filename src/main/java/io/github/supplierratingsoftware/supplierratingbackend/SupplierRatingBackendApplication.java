package io.github.supplierratingsoftware.supplierratingbackend;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenBisProperties.class)
public class SupplierRatingBackendApplication {

    /**
     * The entry point of the SupplierRatingBackendApplication.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SupplierRatingBackendApplication.class, args);
    }
}
