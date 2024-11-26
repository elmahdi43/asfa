package org.asfa.managerasfa.service.mapper;

import org.asfa.managerasfa.domain.MemberSubscription;
import org.asfa.managerasfa.domain.SubscriptionType;
import org.asfa.managerasfa.service.dto.MemberSubscriptionDTO;
import org.asfa.managerasfa.service.dto.SubscriptionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionType} and its DTO {@link SubscriptionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionTypeMapper extends EntityMapper<SubscriptionTypeDTO, SubscriptionType> {
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "memberSubscriptionId")
    SubscriptionTypeDTO toDto(SubscriptionType s);

    @Named("memberSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberSubscriptionDTO toDtoMemberSubscriptionId(MemberSubscription memberSubscription);
}
