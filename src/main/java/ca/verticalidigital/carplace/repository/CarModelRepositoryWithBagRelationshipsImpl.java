package ca.verticalidigital.carplace.repository;

import ca.verticalidigital.carplace.domain.CarModel;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CarModelRepositoryWithBagRelationshipsImpl implements CarModelRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CarModel> fetchBagRelationships(Optional<CarModel> carModel) {
        return carModel.map(this::fetchCategories);
    }

    @Override
    public Page<CarModel> fetchBagRelationships(Page<CarModel> carModels) {
        return new PageImpl<>(fetchBagRelationships(carModels.getContent()), carModels.getPageable(), carModels.getTotalElements());
    }

    @Override
    public List<CarModel> fetchBagRelationships(List<CarModel> carModels) {
        return Optional.of(carModels).map(this::fetchCategories).orElse(Collections.emptyList());
    }

    CarModel fetchCategories(CarModel result) {
        return entityManager
            .createQuery(
                "select carModel from CarModel carModel left join fetch carModel.categories where carModel is :carModel",
                CarModel.class
            )
            .setParameter("carModel", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<CarModel> fetchCategories(List<CarModel> carModels) {
        return entityManager
            .createQuery(
                "select distinct carModel from CarModel carModel left join fetch carModel.categories where carModel in :carModels",
                CarModel.class
            )
            .setParameter("carModels", carModels)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
