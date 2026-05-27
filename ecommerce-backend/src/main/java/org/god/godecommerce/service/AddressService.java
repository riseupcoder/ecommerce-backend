package org.god.godecommerce.service;

import org.god.godecommerce.payload.AddressDTO;
import org.god.godecommerce.payload.AddressResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(Long userId, AddressDTO addressDTO);
    AddressResponse getAllAddress(Pageable pageable);
    AddressDTO getAddressById(Long addressId);
    List<AddressDTO> getUserAddress(Long userId);
    AddressDTO updateAddress(Long userId, Long addressId, AddressDTO addressDTO);
    String  deleteAddress(Long userId, Long addressId);
}
