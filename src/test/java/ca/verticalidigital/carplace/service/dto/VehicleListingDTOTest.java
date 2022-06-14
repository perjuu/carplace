package ca.verticalidigital.carplace.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.verticalidigital.carplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleListingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleListingDTO.class);
        VehicleListingDTO vehicleListingDTO1 = new VehicleListingDTO();
        vehicleListingDTO1.setId(1L);
        VehicleListingDTO vehicleListingDTO2 = new VehicleListingDTO();
        assertThat(vehicleListingDTO1).isNotEqualTo(vehicleListingDTO2);
        vehicleListingDTO2.setId(vehicleListingDTO1.getId());
        assertThat(vehicleListingDTO1).isEqualTo(vehicleListingDTO2);
        vehicleListingDTO2.setId(2L);
        assertThat(vehicleListingDTO1).isNotEqualTo(vehicleListingDTO2);
        vehicleListingDTO1.setId(null);
        assertThat(vehicleListingDTO1).isNotEqualTo(vehicleListingDTO2);
    }
}
