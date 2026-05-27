package org.god.godecommerce.payload;


public record AddressDTO(Long addressId, String street, String city,
                         String state, String postalCode, String country) {
}
