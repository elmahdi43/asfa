package org.asfa.managerasfa.service.mapper;

import org.asfa.managerasfa.domain.MemberSubscription;
import org.asfa.managerasfa.domain.Payment;
import org.asfa.managerasfa.service.dto.MemberSubscriptionDTO;
import org.asfa.managerasfa.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MemberSubscription} and its DTO {@link MemberSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberSubscriptionMapper extends EntityMapper<MemberSubscriptionDTO, MemberSubscription> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    MemberSubscriptionDTO toDto(MemberSubscription s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
