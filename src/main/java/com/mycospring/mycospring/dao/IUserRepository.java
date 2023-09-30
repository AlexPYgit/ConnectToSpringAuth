package com.mycospring.mycospring.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycospring.mycospring.model.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
