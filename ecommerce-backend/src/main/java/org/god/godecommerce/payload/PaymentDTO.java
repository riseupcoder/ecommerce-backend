package org.god.godecommerce.payload;

public record PaymentDTO(
        Long paymentId,
        String paymentMethod,
        String pgName,
        String pgPaymentId,
        String pgStatus,
        String pgResponseMessage
) {
}
