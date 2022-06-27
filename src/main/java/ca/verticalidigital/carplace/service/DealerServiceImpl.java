package ca.verticalidigital.carplace.service;

import ca.verticalidigital.carplace.domain.Dealer;
import ca.verticalidigital.carplace.repository.DealerRepository;
import ca.verticalidigital.carplace.service.dto.DealerDTO;
import ca.verticalidigital.carplace.service.mapper.DealerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DealerServiceImpl implements DealerService {

    private final Logger log = LoggerFactory.getLogger(DealerServiceImpl.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;

    public DealerServiceImpl(
        DealerRepository dealerRepository,
        DealerMapper dealerMapper
    ){
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
    }

    @Override
    public DealerDTO save(DealerDTO dealerDTO) {
        log.debug("Request to save Dealer : {}", dealerDTO);
        Dealer dealer = dealerMapper.toEntity(dealerDTO);
        dealer = dealerRepository.save(dealer);
        return dealerMapper.toDto(dealer);
    }

    @Override
    public DealerDTO update(DealerDTO dealerDTO) {
        log.debug("Request to save Dealer : {}", dealerDTO);
        Dealer dealer = dealerMapper.toEntity(dealerDTO);
        dealer = dealerRepository.save(dealer);
        return dealerMapper.toDto(dealer);
    }

    @Override
    public Optional<DealerDTO> partialUpdate(DealerDTO dealerDTO) {
        log.debug("Request to partially update Dealer : {}", dealerDTO);
        return dealerRepository
            .findById(dealerDTO.getId())
            .map(existingDealer -> {
                dealerMapper.partialUpdate(existingDealer, dealerDTO);
                return  existingDealer;
            })
            .map(dealerRepository::save)
            .map(dealerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dealers");
        return dealerRepository.findAll(pageable).map(dealerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DealerDTO> findOne(Long id) {
        log.debug("Request to get Dealer : {}", id);
        return dealerRepository.findById(id).map(dealerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dealer : {}", id);
        dealerRepository.deleteById(id);
    }
}
