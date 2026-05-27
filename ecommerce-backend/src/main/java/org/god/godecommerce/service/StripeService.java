package org.god.godecommerce.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.god.godecommerce.payload.StripePaymentDTO;

public interface StripeService {

    PaymentIntent createPaymentIntent(StripePaymentDTO stripePaymentDTO) throws StripeException;
}
