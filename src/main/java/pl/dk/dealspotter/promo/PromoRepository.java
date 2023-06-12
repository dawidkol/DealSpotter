package pl.dk.dealspotter.promo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromoRepository extends CrudRepository<Promo, Long> {
    Optional <Promo> findByName(String name);
    Optional <Promo> findByUserId(Long id);
    List<Promo> findPromosByUser_Email(String email);


}
