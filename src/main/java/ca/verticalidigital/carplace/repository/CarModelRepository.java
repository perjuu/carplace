package ca.verticalidigital.carplace.repository;

import ca.verticalidigital.carplace.domain.CarModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CarModel entity.
 */
@Repository
public interface CarModelRepository
    extends CarModelRepositoryWithBagRelationships, JpaRepository<CarModel, Long>, JpaSpecificationExecutor<CarModel> {
    default Optional<CarModel> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<CarModel> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<CarModel> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
