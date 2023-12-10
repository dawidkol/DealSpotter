package pl.dk.dealspotter.promo;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.dealspotter.category.CategoryType;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.promo.dto.SavePromoDto;
import pl.dk.dealspotter.user.UserService;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromoService {

    private final PromoRepository promoRepository;
    private final PromoDtoMapper promoDtoMapper;
    private final UserService userService;

    public List<PromoDto> findAllPromo() {
        return promoRepository.findAllPromosOrderByDateDescending()
                .stream()
                .map(promoDtoMapper::map)
                .toList();
    }

    public List<PromoDto> findByCategory(String categoryName) {
        return promoRepository.findAllByCategory_NameIgnoreCase(categoryName)
                .stream()
                .map(promoDtoMapper::map)
                .toList();
    }

    public List<PromoDto> findByNameAndCategory(String name, String categoryName) {
        if (categoryName.equalsIgnoreCase(CategoryType.All.getDescription())) {
            return promoRepository.findAllByNameContainingIgnoreCase(name)
                    .stream()
                    .map(promoDtoMapper::map)
                    .toList();
        } else {
            return promoRepository.findAllByNameContainingIgnoreCaseAndCategory_NameIgnoreCase(name, categoryName)
                    .stream()
                    .map(promoDtoMapper::map)
                    .toList();
        }
    }

    public List<PromoDto> findPromosByUsername(String email) {
        return promoRepository
                .findPromosByUser_Email(email)
                .stream()
                .map(promoDtoMapper::map)
                .toList();
    }

    public Optional<PromoDto> findById(Long id) {
        return promoRepository
                .findById(id)
                .map(promoDtoMapper::map);
    }

    @Transactional
    public void savePromo(SavePromoDto savePromoDto, String email) {
        if (isAdmin(email) || isUser(email)) {
            Promo promo = promoDtoMapper.map(savePromoDto);
            promoRepository.save(promo);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public void updatePromo(SavePromoDto savePromoDto, String email) {
        Promo promo = promoDtoMapper.map(savePromoDto);
        if (isAdmin(email)) {
            promoRepository.save(promo);
        } else if (checkIfUserPromo(promo.getId(), email)) {
            promoRepository.save(promo);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }


    @Transactional
    public void deletePromo(Long id, String email) {
        Promo promo = promoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (isAdmin(email)) {
            promoRepository.deleteById(id);
        } else if (checkIfUserPromo(promo.getId(), email) && isUser(email)) {
            promoRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private boolean isAdmin(String email) {
        return userService.findCredentialsByEmail(email)
                .stream()
                .map(UserCredentialsDto::getRoles)
                .anyMatch(credential -> credential.stream().anyMatch(c -> c.equalsIgnoreCase("ADMIN")));
    }

    private boolean isUser(String email) {
        return userService.findCredentialsByEmail(email)
                .stream()
                .map(UserCredentialsDto::getRoles)
                .anyMatch(credential -> credential.stream().anyMatch(c -> c.equalsIgnoreCase("USER")));
    }

    private boolean checkIfUserPromo(Long id, String email) {
        return promoRepository.findPromosByUser_Email(email)
                .stream()
                .map(Promo::getId)
                .anyMatch(promoId -> Objects.equals(promoId, id));
    }

}



