package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.entity.Offer;

//ToDo ready
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    
}
