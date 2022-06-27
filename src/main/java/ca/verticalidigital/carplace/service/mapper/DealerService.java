package ca.verticalidigital.carplace.service.mapper;

import ca.verticalidigital.carplace.service.dto.DealerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DealerService {

    DealerDTO save(DealerDTO dealerDTO);

    DealerDTO update(DealerDTO dealerDTO);

    Optional<DealerDTO> partialUpdate(DealerDTO dealerDTO);

    Page<DealerDTO> findAll(Pageable pageable);

    Optional<DealerDTO> findOne(Long id);

    void delete(Long id);
}
