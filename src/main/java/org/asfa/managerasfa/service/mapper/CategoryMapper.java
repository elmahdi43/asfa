package org.asfa.managerasfa.service.mapper;

import org.asfa.managerasfa.domain.Category;
import org.asfa.managerasfa.domain.Product;
import org.asfa.managerasfa.service.dto.CategoryDTO;
import org.asfa.managerasfa.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    CategoryDTO toDto(Category s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
