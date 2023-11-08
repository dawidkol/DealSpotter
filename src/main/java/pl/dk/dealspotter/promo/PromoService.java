package pl.dk.dealspotter.promo;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.promo.dto.SavePromoDto;
import pl.dk.dealspotter.user.UserService;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;

import java.util.Collections;
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
        List<Promo> list = (List<Promo>) promoRepository.findAll();
        Collections.reverse(list);
        return list.stream().map(promoDtoMapper::map).toList();
    }

    public List<PromoDto> findByCategory(String category) {
        if (category.equalsIgnoreCase("Wszystkie kategorie")) {
            List<Promo> allPromoList = (List<Promo>) promoRepository.findAll();
            return allPromoList.stream().map(promoDtoMapper::map).toList();
        } else {
            List<Promo> promosByCategory = (List<Promo>) promoRepository.findAll();
            return promosByCategory.stream().filter(a ->
                            a.getCategory().getName().equalsIgnoreCase(category))
                    .map(promoDtoMapper::map)
                    .toList();
        }
    }

    public List<PromoDto> findByNameAndCategory(String name, String category) {
        List<Promo> list = (List<Promo>) promoRepository.findAll();
        if (category.equals("Wszystkie kategorie")) {
            return list.stream()
                    .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
                    .map(promoDtoMapper::map)
                    .toList();
        }
        return list.stream()
                .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(a -> a.getCategory().getName().equalsIgnoreCase(category))
                .map(promoDtoMapper::map)
                .toList();
    }

    public List<PromoDto> findPromosByUsername() {
        String name = findCurrentUsername();
        return promoRepository
                .findPromosByUser_Email(name)
                .stream()
                .map(promoDtoMapper::map)
                .toList();
    }

    public Optional<PromoDto> getPromoById(Long id) {
        return promoRepository
                .findById(id)
                .map(promoDtoMapper::map);
    }

    @Transactional
    public void savePromo(SavePromoDto savePromoDto) {
        String email = findCurrentUsername();
        if (isAdmin(email) || isUser(email)) {
            Promo promo = promoDtoMapper.map(savePromoDto);
            promoRepository.save(promo);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public void updatePromo(SavePromoDto savePromoDto) {
        String email = findCurrentUsername();
        Promo promo = promoDtoMapper.map(savePromoDto);
        if (isAdmin(email)) {
            promoRepository.save(promo);
        } else if (checkIfUserPromo(promo, email)) {
            promoRepository.save(promo);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public void deletePromo(Long id) {
        String email = findCurrentUsername();
        Promo save = promoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (isAdmin(email)) {
            promoRepository.deleteById(id);
        } else if (checkIfUserPromo(save, email)) {
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

    public boolean checkIfUserPromo(Promo promo, String email) {
        return promoRepository.findPromosByUser_Email(email)
                .stream()
                .map(Promo::getId)
                .anyMatch(id -> Objects.equals(id, promo.getId()));
    }

    private static String findCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}



