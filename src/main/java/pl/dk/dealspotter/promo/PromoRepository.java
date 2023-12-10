package pl.dk.dealspotter.promo;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoRepository extends CrudRepository<Promo, Long> {
    List<Promo> findPromosByUser_Email(String email);
    List<Promo> findAll();

    @Query(value = "SELECT * FROM promo ORDER BY added DESC", nativeQuery = true)
    List<Promo> findAllPromosOrderByDateDescending();

    List<Promo> findAllByCategory_NameIgnoreCase(String name);

    List<Promo> findAllByNameContainingIgnoreCaseAndCategory_NameIgnoreCase(String name, String categoryName);

    List<Promo> findAllByNameContainingIgnoreCase(String name);
}
