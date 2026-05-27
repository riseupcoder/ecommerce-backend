package org.god.godecommerce.controller;

import jakarta.validation.Valid;
import org.god.godecommerce.payload.AddressDTO;
import org.god.godecommerce.payload.AddressResponse;
import org.god.godecommerce.security.jwt.services.UserDetailsImpl;
import org.god.godecommerce.service.AddressService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AddressDTO createdAddress = addressService.createAddress(userDetails.id(), addressDTO);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<AddressResponse> getAllAddress(
            @PageableDefault() Pageable pageable) {
        AddressResponse allAddress = addressService.getAllAddress(pageable);
        return new ResponseEntity<>(allAddress, HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<AddressDTO>> getUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AddressDTO> addressDTOList = addressService.getUserAddress(userDetails.id());
        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressDTO addressDTO
    ) {
        AddressDTO updatedAddress = addressService
                .updateAddress(userDetails.id(), addressId, addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long addressId) {
        String status = addressService.deleteAddress(userDetails.id(), addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
