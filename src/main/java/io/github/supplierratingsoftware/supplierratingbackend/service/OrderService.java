package io.github.supplierratingsoftware.supplierratingbackend.service;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.ProjectSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleTypeSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SpaceSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import io.github.supplierratingsoftware.supplierratingbackend.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for managing order-related business logic.
 * <p>
 * This service communicates with the {@link OpenBisClient} to retrieve raw data
 * and uses the {@link OrderMapper} to transform it into API-compliant DTOs.
 * It encapsulates the specific knowledge about where orders are stored in OpenBIS
 * (Space: LIEFERANTENBEWERTUNG, Project: BESTELLUNGEN).
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OpenBisClient openBisClient;
    private final OrderMapper orderMapper;
    private final OpenBisProperties properties;

    /**
     * Retrieves all orders from OpenBIS.
     * <p>
     * Executes a server-side filtered search for samples located in the configured
     * space, project, and matching the configured sample type. It fetches only the necessary
     * properties and types for the orders. This includes also the order's parent sample (if any).
     * </p>
     *
     * @return A list of {@link OrderDto} objects representing all orders.
     */
    public List<OrderDto> getAllOrders() {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(SpaceSearchCriteria.withCode(properties.search().defaultSpace()))
                .with(ProjectSearchCriteria.withCode(properties.search().orderProject()))
                .with(SampleTypeSearchCriteria.withCode(properties.search().orderType()));
        SampleFetchOptions parentOptions = new SampleFetchOptions(
                null, // No properties needed for parent
                null, // No type information needed for parent
                null // Stop recursion after parent
        );
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(), // Fetch properties of the order
                new SampleTypeFetchOptions(), // Fetch type information of the order
                parentOptions // Fetch parent information
        );
        List<OpenBisSample> rawSamples = openBisClient.searchSamples(criteria, fetchOptions);
        return rawSamples.stream().map(orderMapper::toDto).toList();
    }
}
