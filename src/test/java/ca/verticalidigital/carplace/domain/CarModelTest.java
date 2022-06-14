package ca.verticalidigital.carplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.verticalidigital.carplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarModel.class);
        CarModel carModel1 = new CarModel();
        carModel1.setId(1L);
        CarModel carModel2 = new CarModel();
        carModel2.setId(carModel1.getId());
        assertThat(carModel1).isEqualTo(carModel2);
        carModel2.setId(2L);
        assertThat(carModel1).isNotEqualTo(carModel2);
        carModel1.setId(null);
        assertThat(carModel1).isNotEqualTo(carModel2);
    }
}
