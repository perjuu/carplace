package ca.verticalidigital.carplace.service;

import ca.verticalidigital.carplace.domain.*; // for static metamodels
import ca.verticalidigital.carplace.domain.VehicleListing;
import ca.verticalidigital.carplace.repository.VehicleListingRepository;
import ca.verticalidigital.carplace.service.criteria.VehicleListingCriteria;
import ca.verticalidigital.carplace.service.dto.VehicleListingDTO;
import ca.verticalidigital.carplace.service.mapper.VehicleListingMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link VehicleListing} entities in the database.
 * The main input is a {@link VehicleListingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VehicleListingDTO} or a {@link Page} of {@link VehicleListingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleListingQueryService extends QueryService<VehicleListing> {

    private final Logger log = LoggerFactory.getLogger(VehicleListingQueryService.class);

    private final VehicleListingRepository vehicleListingRepository;

    private final VehicleListingMapper vehicleListingMapper;

    public VehicleListingQueryService(VehicleListingRepository vehicleListingRepository, VehicleListingMapper vehicleListingMapper) {
        this.vehicleListingRepository = vehicleListingRepository;
        this.vehicleListingMapper = vehicleListingMapper;
    }

    /**
     * Return a {@link List} of {@link VehicleListingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VehicleListingDTO> findByCriteria(VehicleListingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VehicleListing> specification = createSpecification(criteria);
        return vehicleListingMapper.toDto(vehicleListingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VehicleListingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleListingDTO> findByCriteria(VehicleListingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VehicleListing> specification = createSpecification(criteria);
        return vehicleListingRepository.findAll(specification, page).map(vehicleListingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleListingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VehicleListing> specification = createSpecification(criteria);
        return vehicleListingRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleListingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VehicleListing> createSpecification(VehicleListingCriteria criteria) {
        Specification<VehicleListing> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VehicleListing_.id));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), VehicleListing_.price));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), VehicleListing_.year));
            }
            if (criteria.getMileage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMileage(), VehicleListing_.mileage));
            }
            if (criteria.getFuel() != null) {
                specification = specification.and(buildSpecification(criteria.getFuel(), VehicleListing_.fuel));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), VehicleListing_.status));
            }
            if (criteria.getCarModelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCarModelId(),
                            root -> root.join(VehicleListing_.carModel, JoinType.LEFT).get(CarModel_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
