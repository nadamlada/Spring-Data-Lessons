package hiberspring.repository;

import hiberspring.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO ready
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Branch findByName(String name);
}
