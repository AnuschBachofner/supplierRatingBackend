package io.github.supplierratingsoftware.supplierratingbackend;

import io.github.supplierratingsoftware.supplierratingbackend.service.OpenBisClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    private final OpenBisClient openBisClient;
    public ConnectionTester(OpenBisClient openBisClient) {
        this.openBisClient = openBisClient;
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("-------------------------------------------------------------");
        System.out.println("         Testing the OpenBIS connection...                 ");
        System.out.println("-------------------------------------------------------------");

        try {
            String sessionToken = openBisClient.login();
            System.out.println("Successfully logged in to OpenBIS with session token: " + sessionToken);
        } catch (Exception e) {
            System.out.println("Failed to log in to OpenBIS: " + e.getMessage());
        }
        System.out.println("-------------------------------------------------------------");
    }
}
