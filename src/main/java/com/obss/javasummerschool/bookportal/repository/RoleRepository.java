package com.obss.javasummerschool.bookportal.repository;

import com.obss.javasummerschool.bookportal.model.Role;
import com.obss.javasummerschool.bookportal.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
