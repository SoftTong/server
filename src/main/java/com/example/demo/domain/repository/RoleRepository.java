package com.example.demo.domain.repository;

import com.example.demo.dao.RoleDao;
import com.example.demo.dao.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <RoleDao, Long> {
    Optional<RoleDao> findByName(RoleName roleName);
}
