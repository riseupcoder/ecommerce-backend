package org.god.godecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @NotBlank
    @Size(min = 4, message = "payment method must contain at least 4 characters.")
    private String paymentMethod;

    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    public Payment(Long paymentId, String pgPaymentId, String pgResponseMessage, String pgStatus, String pgName) {
        this.paymentId = paymentId;
        this.pgPaymentId = pgPaymentId;
        this.pgResponseMessage = pgResponseMessage;
        this.pgStatus = pgStatus;
        this.pgName = pgName;
    }

    public Payment() {
    }
}
