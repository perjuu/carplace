package ca.verticalidigital.carplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.verticalidigital.carplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleListingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleListing.class);
        VehicleListing vehicleListing1 = new VehicleListing();
        vehicleListing1.setId(1L);
        VehicleListing vehicleListing2 = new VehicleListing();
        vehicleListing2.setId(vehicleListing1.getId());
        assertThat(vehicleListing1).isEqualTo(vehicleListing2);
        vehicleListing2.setId(2L);
        assertThat(vehicleListing1).isNotEqualTo(vehicleListing2);
        vehicleListing1.setId(null);
        assertThat(vehicleListing1).isNotEqualTo(vehicleListing2);
    }
}
