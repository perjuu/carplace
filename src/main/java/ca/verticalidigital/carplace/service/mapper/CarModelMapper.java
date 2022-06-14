package ca.verticalidigital.carplace.service.mapper;

import ca.verticalidigital.carplace.domain.CarModel;
import ca.verticalidigital.carplace.domain.Category;
import ca.verticalidigital.carplace.service.dto.CarModelDTO;
import ca.verticalidigital.carplace.service.dto.CategoryDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarModel} and its DTO {@link CarModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarModelMapper extends EntityMapper<CarModelDTO, CarModel> {
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoryNameSet")
    CarModelDTO toDto(CarModel s);

    @Mapping(target = "removeCategory", ignore = true)
    CarModel toEntity(CarModelDTO carModelDTO);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoCategoryName(Category category);

    @Named("categoryNameSet")
    default Set<CategoryDTO> toDtoCategoryNameSet(Set<Category> category) {
        return category.stream().map(this::toDtoCategoryName).collect(Collectors.toSet());
    }
}
