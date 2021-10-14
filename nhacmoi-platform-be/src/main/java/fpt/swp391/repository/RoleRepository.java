package fpt.swp391.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.swp391.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
