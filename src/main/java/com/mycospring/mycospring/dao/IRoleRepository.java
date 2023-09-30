package com.mycospring.mycospring.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycospring.mycospring.model.RoleEntity;
import com.mycospring.mycospring.model.RoleName;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {

	Optional<RoleEntity> findByRole(RoleName role);
}
