package org.god.godecommerce.payload;

public record StripePaymentDTO(Long amount, String currency) {
}
