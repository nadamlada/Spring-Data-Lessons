package hiberspring.repository;

import hiberspring.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO ready
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findEmployeeByFirstNameAndLastNameAndPosition(
            String firstName,
            String lastName,
            String position);
}
