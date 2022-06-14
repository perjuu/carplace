package ca.verticalidigital.carplace.service;

import ca.verticalidigital.carplace.service.dto.CarModelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ca.verticalidigital.carplace.domain.CarModel}.
 */
public interface CarModelService {
    /**
     * Save a carModel.
     *
     * @param carModelDTO the entity to save.
     * @return the persisted entity.
     */
    CarModelDTO save(CarModelDTO carModelDTO);

    /**
     * Updates a carModel.
     *
     * @param carModelDTO the entity to update.
     * @return the persisted entity.
     */
    CarModelDTO update(CarModelDTO carModelDTO);

    /**
     * Partially updates a carModel.
     *
     * @param carModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarModelDTO> partialUpdate(CarModelDTO carModelDTO);

    /**
     * Get all the carModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarModelDTO> findAll(Pageable pageable);

    /**
     * Get all the carModels with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarModelDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" carModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarModelDTO> findOne(Long id);

    /**
     * Delete the "id" carModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
