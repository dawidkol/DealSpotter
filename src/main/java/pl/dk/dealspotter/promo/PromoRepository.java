package pl.dk.dealspotter.promo;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoRepository extends CrudRepository<Promo, Long> {
    List<Promo> findPromosByUser_Email(String email);

    @Query(value = "SELECT * FROM promo ORDER BY added DESC", nativeQuery = true)
    List<Promo> findAllPromosOrderByDateDescending();

    @Query(value = "SELECT promo.* FROM promo JOIN category ON promo.category_id = category.id WHERE LOWER(category.name) = LOWER(:categoryName) ORDER BY promo.added DESC ", nativeQuery = true )
    List<Promo> findAllByCategory(String categoryName);

    List<Promo> findAllByNameContainingIgnoreCaseAndCategory_NameIgnoreCase(String name, String categoryName);

    List<Promo> findAllByNameContainingIgnoreCase(String name);
}
