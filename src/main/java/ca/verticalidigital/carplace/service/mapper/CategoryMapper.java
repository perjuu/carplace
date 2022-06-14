package ca.verticalidigital.carplace.service.mapper;

import ca.verticalidigital.carplace.domain.Category;
import ca.verticalidigital.carplace.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}
