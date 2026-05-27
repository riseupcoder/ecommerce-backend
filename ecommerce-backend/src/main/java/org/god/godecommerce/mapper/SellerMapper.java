package org.god.godecommerce.mapper;

import org.god.godecommerce.model.User;
import org.god.godecommerce.payload.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    UserDTO toDto(User user);
}
