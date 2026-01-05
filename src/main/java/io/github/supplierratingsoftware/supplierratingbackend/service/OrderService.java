package io.github.supplierratingsoftware.supplierratingbackend.service;

import io.github.supplierratingsoftware.supplierratingbackend.config.OpenBisProperties;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderReadDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderUpdateDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.creation.SampleCreation;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.id.SamplePermId;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.PermIdSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.ProjectSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleParentsSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleTypeSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SpaceSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.update.SampleUpdate;
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
     * @return A list of {@link OrderReadDto} objects representing all orders.
     */
    public List<OrderReadDto> getAllOrders(String supplierId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(SpaceSearchCriteria.withCode(properties.defaultSpace()))
                .with(ProjectSearchCriteria.withCode(properties.order().projectCode()))
                .with(SampleTypeSearchCriteria.withCode(properties.order().typeCode()));

        if (supplierId != null && !supplierId.isBlank()) criteria.with(SampleParentsSearchCriteria.withParentId(supplierId));

        SampleFetchOptions childrenOptions = new SampleFetchOptions(
                new PropertyFetchOptions(), // Empty properties
                new SampleTypeFetchOptions(), // Type information
                null,
                null
        );

        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(), // Fetch properties of the order
                new SampleTypeFetchOptions(), // Fetch type information of the order
                new SampleFetchOptions( // Fetch parent (supplier) of the order
                        new PropertyFetchOptions(),
                        new SampleTypeFetchOptions(),
                        null,
                        null),
                childrenOptions // Fetch children (ratings) of the order
        );
        List<OpenBisSample> rawSamples = openBisClient.searchSamples(criteria, fetchOptions);

        // Map to DTOs
        return rawSamples.stream()
                .map(orderMapper::toApiDto)
                .toList();
    }

    /**
     * Retrieves an order by its PermID from OpenBIS.
     * <p>
     * Executes a search for the order sample with the given PermID and the configured
     * order sample type. It fetches the order's properties, its parent supplier sample,
     * and its child rating samples, then maps the result to an {@link OrderReadDto}.
     * </p>
     *
     * @param permId The PermID of the order to retrieve.
     * @return The {@link OrderReadDto} representing the order with the given PermID.
     * @throws OpenBisResourceNotFoundException if the order does not exist.
     */
    public OrderReadDto getOrderById(String permId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(PermIdSearchCriteria.withId(permId))
                .with(SampleTypeSearchCriteria.withCode(properties.order().typeCode()));

        // Children (ratings)
        SampleFetchOptions childrenOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                null
        );

        // Parents (supplier)
        SampleFetchOptions parentOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                null
        );

        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                parentOptions, // Fetch parent (supplier) of the order
                childrenOptions // Fetch children (ratings) of the order
        );

        List<OpenBisSample> rawSamples = openBisClient.searchSamples(criteria, fetchOptions);

        if (rawSamples.isEmpty()) {
            throw new OpenBisResourceNotFoundException("Order with PermID " + permId + " not found.");
        }

        return orderMapper.toApiDto(rawSamples.getFirst());
    }

    /**
     * Creates a new order in OpenBIS based on the provided data.
     *
     * @param creationDto The data for the new order.
     * @return The created order DTO (fetched fresh from openBIS to ensure consistency).
     */
    public OrderReadDto createOrder(OrderCreationDto creationDto) {
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
     * Updates an existing order in OpenBIS based on the provided data.
     *
     * @param permId    The PermID of the order to update.
     * @param updateDto The update data for the order.
     * @return The updated order details.
     * @throws OpenBisResourceNotFoundException if the order does not exist.
     */
    public OrderReadDto updateOrder(String permId, OrderUpdateDto updateDto) {
        log.info("Updating order with PermID: {}", permId);

        // Validate Order Existence
        checkOrderExists(permId);

        // Map to OpenBIS Update object
        SampleUpdate update = orderMapper.toOpenBisUpdate(permId, updateDto);

        // Execute Update via Client
        openBisClient.updateSamples(List.of(update));

        // Fetch and return fresh order
        return fetchFreshOrder(permId);
    }

    /**
     * Checks if an order with the given PermID exists in OpenBIS.
     *
     * @param permId The PermID of the order to check.
     * @throws OpenBisResourceNotFoundException if the order does not exist.
     */
    private void checkOrderExists(String permId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(PermIdSearchCriteria.withId(permId))
                .with(SampleTypeSearchCriteria.withCode(properties.order().typeCode()));

        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(),
                new SampleTypeFetchOptions(),
                null,
                null
        );

        List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);
        if (results.isEmpty()) {
            throw new OpenBisResourceNotFoundException("Order with PermID " + permId + " not found.");
        }
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
    private OrderReadDto fetchFreshOrder(String permID) {
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

    /**
     * Checks if a supplier with the given PermID exists in OpenBIS.
     *
     * @param supplierId The PermID of the supplier to check.
     * @throws OpenBisResourceNotFoundException if the supplier does not exist.
     */
    private void checkSupplierExists(String supplierId) {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(PermIdSearchCriteria.withId(supplierId))
                .with(SampleTypeSearchCriteria.withCode(properties.supplier().typeCode()));
        // Fetch only the ID
        SampleFetchOptions fetchOptions = new SampleFetchOptions(
                new PropertyFetchOptions(), // Fetch basic properties (even if empty list)
                new SampleTypeFetchOptions(), // Fetch type information
                null,
                null
        );
        List<OpenBisSample> results = openBisClient.searchSamples(criteria, fetchOptions);
        if (results.isEmpty()) {
            throw new OpenBisResourceNotFoundException("Supplier with PermID " + supplierId + " not found.");
        }
    }
}
