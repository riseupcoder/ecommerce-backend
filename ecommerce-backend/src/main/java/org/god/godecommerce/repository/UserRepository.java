package org.god.godecommerce.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.god.godecommerce.model.AppRole;
import org.god.godecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(@NotBlank @Size(max = 20) String userName);

    boolean existsByUserName(String s);

    boolean existsByEmail(String s);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :roleName")
    Page<User> findAllByRoleName(@Param("roleName") AppRole appRole,
                                 Pageable pageable);
}
