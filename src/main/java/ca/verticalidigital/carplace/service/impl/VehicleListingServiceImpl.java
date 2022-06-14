package ca.verticalidigital.carplace.service.impl;

import ca.verticalidigital.carplace.domain.VehicleListing;
import ca.verticalidigital.carplace.repository.VehicleListingRepository;
import ca.verticalidigital.carplace.service.VehicleListingService;
import ca.verticalidigital.carplace.service.dto.VehicleListingDTO;
import ca.verticalidigital.carplace.service.mapper.VehicleListingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VehicleListing}.
 */
@Service
@Transactional
public class VehicleListingServiceImpl implements VehicleListingService {

    private final Logger log = LoggerFactory.getLogger(VehicleListingServiceImpl.class);

    private final VehicleListingRepository vehicleListingRepository;

    private final VehicleListingMapper vehicleListingMapper;

    public VehicleListingServiceImpl(VehicleListingRepository vehicleListingRepository, VehicleListingMapper vehicleListingMapper) {
        this.vehicleListingRepository = vehicleListingRepository;
        this.vehicleListingMapper = vehicleListingMapper;
    }

    @Override
    public VehicleListingDTO save(VehicleListingDTO vehicleListingDTO) {
        log.debug("Request to save VehicleListing : {}", vehicleListingDTO);
        VehicleListing vehicleListing = vehicleListingMapper.toEntity(vehicleListingDTO);
        vehicleListing = vehicleListingRepository.save(vehicleListing);
        return vehicleListingMapper.toDto(vehicleListing);
    }

    @Override
    public VehicleListingDTO update(VehicleListingDTO vehicleListingDTO) {
        log.debug("Request to save VehicleListing : {}", vehicleListingDTO);
        VehicleListing vehicleListing = vehicleListingMapper.toEntity(vehicleListingDTO);
        vehicleListing = vehicleListingRepository.save(vehicleListing);
        return vehicleListingMapper.toDto(vehicleListing);
    }

    @Override
    public Optional<VehicleListingDTO> partialUpdate(VehicleListingDTO vehicleListingDTO) {
        log.debug("Request to partially update VehicleListing : {}", vehicleListingDTO);

        return vehicleListingRepository
            .findById(vehicleListingDTO.getId())
            .map(existingVehicleListing -> {
                vehicleListingMapper.partialUpdate(existingVehicleListing, vehicleListingDTO);

                return existingVehicleListing;
            })
            .map(vehicleListingRepository::save)
            .map(vehicleListingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleListingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleListings");
        return vehicleListingRepository.findAll(pageable).map(vehicleListingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleListingDTO> findOne(Long id) {
        log.debug("Request to get VehicleListing : {}", id);
        return vehicleListingRepository.findById(id).map(vehicleListingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleListing : {}", id);
        vehicleListingRepository.deleteById(id);
    }
}
