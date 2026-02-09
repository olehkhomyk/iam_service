package com.post_hub.iam_service.repository;

import com.post_hub.iam_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Optional<User> findByIdAndDeletedFalse(Integer id);

	Optional<User> findByEmailAndDeletedFalse(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);
}
