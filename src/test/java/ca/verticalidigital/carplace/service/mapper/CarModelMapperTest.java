package ca.verticalidigital.carplace.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarModelMapperTest {

    private CarModelMapper carModelMapper;

    @BeforeEach
    public void setUp() {
        carModelMapper = new CarModelMapperImpl();
    }
}
