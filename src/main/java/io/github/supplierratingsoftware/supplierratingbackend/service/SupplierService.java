package io.github.supplierratingsoftware.supplierratingbackend.service;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.PropertyFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.fetchoptions.SampleTypeFetchOptions;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.result.OpenBisSample;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.ProjectSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SampleSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.dto.openbis.search.SpaceSearchCriteria;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import io.github.supplierratingsoftware.supplierratingbackend.mapper.SupplierMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for managing supplier-related business logic.
 * <p>
 * This service communicates with the {@link OpenBisClient} to retrieve raw data
 * and uses the {@link SupplierMapper} to transform it into API-compliant DTOs.
 * It encapsulates the specific knowledge about where suppliers are stored in OpenBIS
 * (Space: LIEFERANTENBEWERTUNG, Project: LIEFERANTEN).
 * </p>
 */
@Service
public class SupplierService {
    private final OpenBisClient openBisClient;
    private final SupplierMapper supplierMapper;
    private static final String SPACE_CODE = "LIEFERANTENBEWERTUNG";
    private static final String PROJECT_CODE = "LIEFERANTEN";

    /**
     * Constructor.
     *
     * @param openBisClient  The OpenBIS client for data retrieval.
     * @param supplierMapper The mapper for converting OpenBIS samples to API DTOs.
     */
    public SupplierService(OpenBisClient openBisClient, SupplierMapper supplierMapper) {
        this.openBisClient = openBisClient;
        this.supplierMapper = supplierMapper;
    }

    /**
     * Retrieves all suppliers from OpenBIS.
     * <p>
     * Executes a server-side filtered search for samples located in the configured
     * space and project.
     * </p>
     *
     * @return A list of {@link SupplierDto} objects representing all available suppliers.
     */
    public List<SupplierDto> getAllSuppliers() {
        SampleSearchCriteria criteria = SampleSearchCriteria.create()
                .with(SpaceSearchCriteria.withCode(SPACE_CODE))
                .with(ProjectSearchCriteria.withCode(PROJECT_CODE));
        SampleFetchOptions fetchOptions = new SampleFetchOptions(new PropertyFetchOptions(), new SampleTypeFetchOptions());
        List<OpenBisSample> rawSamples = openBisClient.searchSamples(criteria, fetchOptions);
        return rawSamples.stream().map(supplierMapper::toDto).toList();
    }
}
