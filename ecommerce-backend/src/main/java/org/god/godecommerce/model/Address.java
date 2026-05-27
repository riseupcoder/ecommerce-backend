package org.god.godecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "street name must contain at least five characters.")
    private String street;

    @NotBlank
    @Size(min = 4, message = "city name must contain at least 4 characters.")
    private String city;

    @NotBlank
    @Size(min = 2, message = "state name must contain at least 2 characters.")
    private String state;

    @NotBlank
    @Size(min = 6, message = "postal code must contain at least 6 characters.")
    private String postalCode;

    @NotBlank
    @Size(min = 2, message = "country name must contain at least 2 characters.")
    private String country;

    public Address() {
    }

    public Address(String street, String city, String state, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
