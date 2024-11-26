package org.asfa.managerasfa.service.mapper;

import org.asfa.managerasfa.domain.Payment;
import org.asfa.managerasfa.domain.PaymentMethod;
import org.asfa.managerasfa.service.dto.PaymentDTO;
import org.asfa.managerasfa.service.dto.PaymentMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    PaymentMethodDTO toDto(PaymentMethod s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
