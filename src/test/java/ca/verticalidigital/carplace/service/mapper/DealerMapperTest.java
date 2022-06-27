package ca.verticalidigital.carplace.service.mapper;

import ca.verticalidigital.carplace.service.DealerMapper;
import ca.verticalidigital.carplace.service.DealerMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class DealerMapperTest {

    private DealerMapper dealerMapper;

    @BeforeEach
    public void setUp(){
        dealerMapper = new DealerMapperImpl();
    }
}
