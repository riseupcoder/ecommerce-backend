package org.god.godecommerce.payload;

public record OrderRequestDTO(
        Long addressId,
        String paymentMethod,
        String pgName,
        String pgPaymentId,
        String pgStatus,
        String pgResponseMessage
) {
}
