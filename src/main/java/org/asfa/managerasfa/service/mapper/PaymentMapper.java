package org.asfa.managerasfa.service.mapper;

import org.asfa.managerasfa.domain.Payment;
import org.asfa.managerasfa.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {}
