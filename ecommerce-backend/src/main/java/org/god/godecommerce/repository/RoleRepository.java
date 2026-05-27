package org.god.godecommerce.repository;

import org.god.godecommerce.model.AppRole;
import org.god.godecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(AppRole roleName);
}
