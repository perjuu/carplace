package ca.verticalidigital.carplace.service.dto;

import ca.verticalidigital.carplace.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DealerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DealerDTO.class);
        DealerDTO dt1 = new DealerDTO();
        dt1.setId(1L);
        DealerDTO dt2 = new DealerDTO();
        assertThat(dt1).isNotEqualTo(dt2);
        dt2.setId(dt1.getId());
        assertThat(dt1).isEqualTo(dt2);
        dt2.setId(2L);
        assertThat(dt1).isNotEqualTo(dt2);
        dt1.setId(null);
        assertThat(dt1).isNotEqualTo(dt2);
    }
}
