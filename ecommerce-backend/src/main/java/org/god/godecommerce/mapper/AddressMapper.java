package org.god.godecommerce.mapper;

import org.god.godecommerce.model.Address;
import org.god.godecommerce.payload.AddressDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "addressId", ignore = true)
    Address toEntity(AddressDTO addressDTO);

    AddressDTO toDto(Address address);

    @Mapping(target = "addressId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AddressDTO addressDTO, @MappingTarget Address address);
}
