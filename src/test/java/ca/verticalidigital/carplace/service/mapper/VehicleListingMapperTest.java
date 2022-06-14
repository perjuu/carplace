package ca.verticalidigital.carplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleListingMapperTest {

    private VehicleListingMapper vehicleListingMapper;

    @BeforeEach
    public void setUp() {
        vehicleListingMapper = new VehicleListingMapperImpl();
    }
}
