package pl.dk.dealspotter.promo;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.dk.dealspotter.category.Category;
import pl.dk.dealspotter.category.CategoryRepository;
import pl.dk.dealspotter.user.UserDtoMapper;
import pl.dk.dealspotter.user.UserRepository;
import pl.dk.dealspotter.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
class PromoDtoMapper {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public PromoDtoMapper(CategoryRepository categoryRepository, UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    PromoDto map(Promo promo) {
        PromoDto promoDto = new PromoDto();
        promoDto.setId(promo.getId());
        promoDto.setName(promo.getName());
        promoDto.setDescription(promo.getDescription());
        promoDto.setPrice(promo.getPrice());
        promoDto.setUrlAddress(promo.getUrlAddress());
        promoDto.setCategory(promo.getCategory().getName());
        promoDto.setImageFilename(promo.getImageFilename());
        UserDto user = userRepository.findById(promo.getUser().getId())
                .map(userDtoMapper::map).orElseThrow(NoSuchElementException::new);
        promoDto.setUserDto(user);
        promoDto.setLocalDateTime(promo.getLocalDateTime());
        return promoDto;
    }

    Promo map(SavePromoDto savePromoDto) {
        Promo promo = new Promo();
        promo.setId(savePromoDto.getId());
        promo.setName(savePromoDto.getName());
        promo.setDescription(savePromoDto.getDescription());
        promo.setPrice(savePromoDto.getPrice());
        promo.setUrlAddress(savePromoDto.getUrlAddress());
        Category category = categoryRepository.findByName(savePromoDto.getCategory()).orElseThrow(NoSuchElementException::new);

        promo.setCategory(category);
        if (savePromoDto.getImageFilename().isEmpty()) {
            promo.setImageFilename(null);
        } else {
            promo.setImageFilename(savePromoDto.getImageFilename());
        }
        promo.setUser(userRepository.findByEmail(SecurityContextHolder
                .getContext().getAuthentication().getName()).orElseThrow(NoSuchElementException::new));
        promo.setLocalDateTime(LocalDateTime.now());
        return promo;
    }
}
