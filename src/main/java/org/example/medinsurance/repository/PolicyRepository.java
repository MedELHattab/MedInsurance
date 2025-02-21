package org.example.medinsurance.repository;

import org.example.medinsurance.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByName(String name);
}
