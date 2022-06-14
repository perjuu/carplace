package ca.verticalidigital.carplace.service;

import ca.verticalidigital.carplace.domain.*; // for static metamodels
import ca.verticalidigital.carplace.domain.CarModel;
import ca.verticalidigital.carplace.repository.CarModelRepository;
import ca.verticalidigital.carplace.service.criteria.CarModelCriteria;
import ca.verticalidigital.carplace.service.dto.CarModelDTO;
import ca.verticalidigital.carplace.service.mapper.CarModelMapper;
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
 * Service for executing complex queries for {@link CarModel} entities in the database.
 * The main input is a {@link CarModelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CarModelDTO} or a {@link Page} of {@link CarModelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarModelQueryService extends QueryService<CarModel> {

    private final Logger log = LoggerFactory.getLogger(CarModelQueryService.class);

    private final CarModelRepository carModelRepository;

    private final CarModelMapper carModelMapper;

    public CarModelQueryService(CarModelRepository carModelRepository, CarModelMapper carModelMapper) {
        this.carModelRepository = carModelRepository;
        this.carModelMapper = carModelMapper;
    }

    /**
     * Return a {@link List} of {@link CarModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CarModelDTO> findByCriteria(CarModelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CarModel> specification = createSpecification(criteria);
        return carModelMapper.toDto(carModelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CarModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CarModelDTO> findByCriteria(CarModelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CarModel> specification = createSpecification(criteria);
        return carModelRepository.findAll(specification, page).map(carModelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarModelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CarModel> specification = createSpecification(criteria);
        return carModelRepository.count(specification);
    }

    /**
     * Function to convert {@link CarModelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CarModel> createSpecification(CarModelCriteria criteria) {
        Specification<CarModel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CarModel_.id));
            }
            if (criteria.getMake() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMake(), CarModel_.make));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), CarModel_.model));
            }
            if (criteria.getLaunchYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLaunchYear(), CarModel_.launchYear));
            }
            if (criteria.getVehicleListingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVehicleListingId(),
                            root -> root.join(CarModel_.vehicleListings, JoinType.LEFT).get(VehicleListing_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(CarModel_.categories, JoinType.LEFT).get(Category_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
