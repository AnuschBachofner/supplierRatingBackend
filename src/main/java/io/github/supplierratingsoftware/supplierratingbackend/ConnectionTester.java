package io.github.supplierratingsoftware.supplierratingbackend;

import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.ProjectSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SpaceSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * NOTE: This class is not part of the application's business logic. Its only purpose is to showcase the OpenBIS connection process.
 *
 * A class responsible for testing the connection to the OpenBIS system during application startup.
 * Implements the {@link CommandLineRunner} interface to execute connection testing when the application starts.
 * Leverages the {@link OpenBisClient} to manage the OpenBIS login functionality.
 *
 * The {@code run} method logs an attempt to connect to the OpenBIS system, aiming to ensure that
 * the service is reachable and correctly configured. The login outcome is printed to the console.
 *
 * This component is automatically recognized and executed by the Spring Framework due to the {@code @Component} annotation.
 */
@Component
public class ConnectionTester implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionTester.class);
    private final OpenBisClient openBisClient;

    public ConnectionTester(OpenBisClient openBisClient) {
        this.openBisClient = openBisClient;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("-------------------------------------------------------------");
        logger.info("         Testing the OpenBIS connection...                 ");
        logger.info("-------------------------------------------------------------");

        try {
            String sessionToken = openBisClient.login();
            logger.info("Successfully logged in to OpenBIS with session token: {}", sessionToken);
        } catch (Exception e) {
            logger.error("Failed to log in to OpenBIS: {}", e.getMessage());
        }

        logger.info("-------------------------------------------------------------");
        logger.info("                 Start openBIS search test...                ");
        logger.info("-------------------------------------------------------------");

        try {
            SampleSearchCriteria criteria = SampleSearchCriteria.create()
                    .with(SpaceSearchCriteria.withCode("LIEFERANTENBEWERTUNG"))
                    .with(ProjectSearchCriteria.withCode("LIEFERANTEN"));

            SampleFetchOptions fetchOptions = new SampleFetchOptions(
                    new PropertyFetchOptions(),
                    new SampleTypeFetchOptions()
            );

            logger.info("Searching for samples in Project 'LIEFERANTEN' (Space: LIEFERANTENBEWERTUNG)...");

            List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);

            logger.info("Found {} samples directly from server.", results.size());

            results.forEach(sample -> {
                logger.info(" - Sample: {} (Type: {})", sample.code(), (sample.type() != null ? sample.type().code() : "?"));
            });

        } catch (Exception e) {
            logger.error("Failed to search", e);
        }

        logger.info("-------------------------------------------------------------");
        logger.info("                 OpenBIS connection test completed!             ");
        logger.info("-------------------------------------------------------------");
    }
}
