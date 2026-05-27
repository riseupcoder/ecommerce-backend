package org.god.godecommerce.mapper;

import org.god.godecommerce.model.Payment;
import org.god.godecommerce.payload.PaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "paymentId", ignore = true)
    Payment toEntity(PaymentDTO paymentDTO);

    PaymentDTO toDto(Payment payment);
}
