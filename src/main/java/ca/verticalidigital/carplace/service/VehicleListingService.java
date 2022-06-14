package ca.verticalidigital.carplace.service;

import ca.verticalidigital.carplace.service.dto.VehicleListingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ca.verticalidigital.carplace.domain.VehicleListing}.
 */
public interface VehicleListingService {
    /**
     * Save a vehicleListing.
     *
     * @param vehicleListingDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleListingDTO save(VehicleListingDTO vehicleListingDTO);

    /**
     * Updates a vehicleListing.
     *
     * @param vehicleListingDTO the entity to update.
     * @return the persisted entity.
     */
    VehicleListingDTO update(VehicleListingDTO vehicleListingDTO);

    /**
     * Partially updates a vehicleListing.
     *
     * @param vehicleListingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleListingDTO> partialUpdate(VehicleListingDTO vehicleListingDTO);

    /**
     * Get all the vehicleListings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleListingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehicleListing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleListingDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleListing.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
