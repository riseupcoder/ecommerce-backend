package org.god.godecommerce.service;

import org.god.godecommerce.exception.ResourceNotFoundException;
import org.god.godecommerce.mapper.AddressMapper;
import org.god.godecommerce.model.Address;
import org.god.godecommerce.model.User;
import org.god.godecommerce.payload.AddressDTO;
import org.god.godecommerce.payload.AddressResponse;
import org.god.godecommerce.repository.AddressRepository;
import org.god.godecommerce.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public AddressDTO createAddress(Long userId, AddressDTO addressDTO) {
        Address address = addressMapper.toEntity(addressDTO);
        User user = userRepository.findById(userId).orElseThrow();
        address.setUser(user);
        addressRepository.save(address);
        return addressMapper.toDto(address);
    }

    @Override
    public AddressResponse getAllAddress(Pageable pageable) {
        Page<Address> addressPage = addressRepository.findAll(pageable);

        List<AddressDTO> addressDTOS = addressPage.getContent().stream().map(addressMapper::toDto).toList();

        return new AddressResponse(addressDTOS, addressPage.getNumber(), addressPage.getSize(), addressPage.getTotalElements(), addressPage.getTotalPages(), addressPage.isLast());
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return addressMapper.toDto(address);
    }

    @Override
    public List<AddressDTO> getUserAddress(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<Address> address = addressRepository.findByUserId(userId);

        List<AddressDTO> addressDTOS = address.stream()
                .map(addressMapper::toDto)
                .toList();
        return addressDTOS;
    }

    @Transactional
    @Override
    public AddressDTO updateAddress(Long userId, Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findByAddressIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressMapper.updateEntityFromDto(addressDTO, address);
        return addressMapper.toDto(address);
    }

    @Transactional
    @Override
    public String deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findByAddressIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressRepository.delete(address);
        return "Address with addressId: " + addressId + " deleted successfully.";
    }
}
