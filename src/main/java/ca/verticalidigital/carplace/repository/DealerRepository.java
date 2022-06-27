package ca.verticalidigital.carplace.repository;

import ca.verticalidigital.carplace.domain.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
    Optional<Dealer> findByName(String name);
    Optional<Dealer> findByCity(String city);
    Optional<Dealer> findByAddress(String address);

}
