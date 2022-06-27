package ca.verticalidigital.carplace.service;

import ca.verticalidigital.carplace.domain.Dealer;
import ca.verticalidigital.carplace.service.dto.DealerDTO;
import ca.verticalidigital.carplace.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DealerMapper extends EntityMapper<DealerDTO, Dealer> {
    public List<DealerDTO> dealersToDealersDTOs(List<Dealer> dealers);

}
