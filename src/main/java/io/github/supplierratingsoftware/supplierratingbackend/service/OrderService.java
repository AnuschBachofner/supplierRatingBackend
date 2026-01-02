package io.github.supplierratingsoftware.supplierratingbackend.service;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.creation.SampleCreation;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.SamplePermId;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.PermIdSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.ProjectSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleTypeSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SpaceSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.exception.OpenBisIntegrationException;
import io.github.supplierratingsoftware.supplierratingbackend.exception.OpenBisResourceNotFoundException;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import io.github.supplierratingsoftware.supplierratingbackend.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

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
     * @param supplierId (Optional) The PermID of the supplier to filter by. If null or blank, returns all orders.
     * @return A list of {@link OrderDto} objects representing all orders.
     */
    public List<OrderDto> getAllOrders(String supplierId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(SpaceSearchCriteria.withCode(properties.defaultSpace()))
                .with(ProjectSearchCriteria.withCode(properties.order().projectCode()))
                .with(SampleTypeSearchCriteria.withCode(properties.order().typeCode()));
        SampleFetchOptions parentOptions = new SampleFetchOptions(
                null, // No properties needed for parent
                null, // No type information needed for parent
                null, // Stop recursion after parent
                null // No children needed for parent
        );
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(), // Fetch properties of the order
                new SampleTypeFetchOptions(), // Fetch type information of the order
                parentOptions, // Fetch parent information
                null // No children needed for orders
        );
        List<OpenBisSample> rawSamples = openBisClient.searchSamples(criteria, fetchOptions);

        // Map to DTOs and filter if a supplier ID was provided
        return rawSamples.stream()
                .map(orderMapper::toApiDto)
                .filter(order -> {
                    // Case: No filter requested -> Include all orders
                    if (supplierId == null || supplierId.isBlank()) return true;
                    // Case: Filter requested -> Include only orders for the given supplier
                    return supplierId.equals(order.supplierId());
                })
                .toList();
    }

    /**
     * Creates a new order in OpenBIS based on the provided data.
     *
     * @param creationDto The data for the new order.
     * @return The created order DTO (fetched fresh from openBIS to ensure consistency).
     */
    public OrderDto createOrder(OrderCreationDto creationDto) {
        log.info("Creating a new order '{}' for supplier '{}'", creationDto.name(), creationDto.supplierId());

        // Validate Supplier Existence
        checkSupplierExists(creationDto.supplierId());

        // Map to openBIS creation object
        SampleCreation creation = orderMapper.toOpenBisCreation(creationDto);

        // Execute Creation via Client
        List<SamplePermId> createdIds = openBisClient.createSamples(List.of(creation));

        if (createdIds.isEmpty()) throw new OpenBisIntegrationException("Creation failed: No PermID returned from openBIS");
        SamplePermId permId = createdIds.getFirst();
        log.info("Successfully created order with PermID: {}", permId.permId());

        // Fetch and return fresh order
        return fetchFreshOrder(permId.permId());
    }

    /**
     * Optimized fetch of fresh order from OpenBIS using the provided PermID.
     * <p>
     * Does NOT fetch children (ratings) as they don't yet exist.
     * It only fetches
     * <ul>
     *     <li>Properties of the Order Sample</li>
     *     <li>Type Information of the Order Sample</li>
     *     <li>parent (Supplier) for ID mapping</li>
     * </ul>
     *
     * @param permID The PermID of the order to fetch.
     * @return The fetched order DTO.
     */
    private OrderDto fetchFreshOrder(String permID) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create().with(PermIdSearchCriteria.withId(permID));
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                new SampleFetchOptions( // Fetch parent (Supplier) only for ID mapping
                        new PropertyFetchOptions(),
                        new SampleTypeFetchOptions(),
                        null, // No Grandparents
                        null // No Siblings
                ),
                null // No Children (Ratings)
        );
        List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);
        if (results.isEmpty()) {
            throw new OpenBisIntegrationException("Critical Error: Could not retrieve fresh order with PermID " + permID + ".");
        }
        return orderMapper.toApiDto(results.getFirst());
    }

    private void checkSupplierExists(String supplierId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(PermIdSearchCriteria.withId(supplierId))
                .with(SampleTypeSearchCriteria.withCode(properties.supplier().typeCode()));
        // Fetch only the ID
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                null,
                null,
                null,
                null
        );
        List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);
        if (results.isEmpty()) {
            throw new OpenBisResourceNotFoundException("Supplier with PermID " + supplierId + " not found.");
        }
    }
}
