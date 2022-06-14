package ca.verticalidigital.carplace.service.impl;

import ca.verticalidigital.carplace.domain.CarModel;
import ca.verticalidigital.carplace.repository.CarModelRepository;
import ca.verticalidigital.carplace.service.CarModelService;
import ca.verticalidigital.carplace.service.dto.CarModelDTO;
import ca.verticalidigital.carplace.service.mapper.CarModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CarModel}.
 */
@Service
@Transactional
public class CarModelServiceImpl implements CarModelService {

    private final Logger log = LoggerFactory.getLogger(CarModelServiceImpl.class);

    private final CarModelRepository carModelRepository;

    private final CarModelMapper carModelMapper;

    public CarModelServiceImpl(CarModelRepository carModelRepository, CarModelMapper carModelMapper) {
        this.carModelRepository = carModelRepository;
        this.carModelMapper = carModelMapper;
    }

    @Override
    public CarModelDTO save(CarModelDTO carModelDTO) {
        log.debug("Request to save CarModel : {}", carModelDTO);
        CarModel carModel = carModelMapper.toEntity(carModelDTO);
        carModel = carModelRepository.save(carModel);
        return carModelMapper.toDto(carModel);
    }

    @Override
    public CarModelDTO update(CarModelDTO carModelDTO) {
        log.debug("Request to save CarModel : {}", carModelDTO);
        CarModel carModel = carModelMapper.toEntity(carModelDTO);
        carModel = carModelRepository.save(carModel);
        return carModelMapper.toDto(carModel);
    }

    @Override
    public Optional<CarModelDTO> partialUpdate(CarModelDTO carModelDTO) {
        log.debug("Request to partially update CarModel : {}", carModelDTO);

        return carModelRepository
            .findById(carModelDTO.getId())
            .map(existingCarModel -> {
                carModelMapper.partialUpdate(existingCarModel, carModelDTO);

                return existingCarModel;
            })
            .map(carModelRepository::save)
            .map(carModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarModels");
        return carModelRepository.findAll(pageable).map(carModelMapper::toDto);
    }

    public Page<CarModelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return carModelRepository.findAllWithEagerRelationships(pageable).map(carModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarModelDTO> findOne(Long id) {
        log.debug("Request to get CarModel : {}", id);
        return carModelRepository.findOneWithEagerRelationships(id).map(carModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarModel : {}", id);
        carModelRepository.deleteById(id);
    }
}
