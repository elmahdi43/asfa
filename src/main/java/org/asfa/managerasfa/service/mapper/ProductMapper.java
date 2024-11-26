package org.asfa.managerasfa.service.mapper;

import org.asfa.managerasfa.domain.MemberSubscription;
import org.asfa.managerasfa.domain.Product;
import org.asfa.managerasfa.service.dto.MemberSubscriptionDTO;
import org.asfa.managerasfa.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "memberSubscriptionId")
    ProductDTO toDto(Product s);

    @Named("memberSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberSubscriptionDTO toDtoMemberSubscriptionId(MemberSubscription memberSubscription);
}
