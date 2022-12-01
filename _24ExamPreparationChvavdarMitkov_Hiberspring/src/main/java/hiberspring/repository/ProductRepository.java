package hiberspring.repository;

import hiberspring.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO ready
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
