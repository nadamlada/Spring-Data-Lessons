package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Offer;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    //TODO: this query is not working
//List<Offer> findAllByApartment_ApartmentType_Three_roomsOrderByApartment_AreaDescPrice();
//List<Offer> findAllByApartment_ApartmentType_Three_roomsOrderByApartment
//    @Query("""
//            SELECT o
//            FROM Offer o
//            WHERE o.apartment.apartmentType = "three_rooms"
//            ORDER BY o.apartment.area DESC, o.price
//            """)
//    List<Offer> lastQuery();
}
