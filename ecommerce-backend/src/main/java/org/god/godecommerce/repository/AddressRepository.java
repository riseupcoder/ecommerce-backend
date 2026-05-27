package org.god.godecommerce.repository;

import org.god.godecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a where a.user.userId = :userId")
    List<Address> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Address a where a.addressId = :addressId AND a.user.userId = :userId")
   Optional<Address> findByAddressIdAndUserId(@Param("addressId") Long addressId, @Param("userId") Long userId);
}
