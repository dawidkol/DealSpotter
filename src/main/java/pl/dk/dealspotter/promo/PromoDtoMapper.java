package pl.dk.dealspotter.promo;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.dealspotter.category.Category;
import pl.dk.dealspotter.category.CategoryRepository;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.promo.dto.SavePromoDto;
import pl.dk.dealspotter.security.SecurityService;
import pl.dk.dealspotter.user.User;
import pl.dk.dealspotter.user.UserDtoMapper;
import pl.dk.dealspotter.user.UserNotFoundException;
import pl.dk.dealspotter.user.UserRepository;
import pl.dk.dealspotter.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
class PromoDtoMapper {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    PromoDto map(Promo promo) {
        return PromoDto.builder()
                .id(promo.getId())
                .name(promo.getName())
                .description(promo.getDescription())
                .price(promo.getPrice())
                .urlAddress(promo.getUrlAddress())
                .category(promo.getCategory().getName())
                .imageFilename(promo.getImageFilename())
                .userDto(getUserId(promo))
                .added(promo.getAdded())
                .build();
    }

    private UserDto getUserId(Promo promo) {
        return userRepository.findById(promo.getUser().getId())
                .map(userDtoMapper::map)
                .orElseThrow(UserNotFoundException::new);
    }

    Promo map(SavePromoDto savePromoDto) {
        return Promo.builder()
                .id(savePromoDto.getId())
                .name(savePromoDto.getName())
                .description(savePromoDto.getDescription())
                .price(savePromoDto.getPrice())
                .urlAddress(savePromoDto.getUrlAddress())
                .category(getCategory(savePromoDto))
                .imageFilename(setImage(savePromoDto))
                .user(findCurrentUsername())
                .added(LocalDateTime.now())
                .build();
    }

    private Category getCategory(SavePromoDto savePromoDto) {
        return categoryRepository.findByNameIgnoreCase(savePromoDto.getCategory())
                .orElseThrow(NoSuchElementException::new);
    }

    private User findCurrentUsername() {
        return userRepository.findByEmail(SecurityService.findCurrentUsername())
                .orElseThrow(UserNotFoundException::new);
    }

    private String setImage(SavePromoDto savePromoDto) {
        if (savePromoDto.getImageFilename().isEmpty()) {
            savePromoDto.setImageFilename("empty-image.png");
            return savePromoDto.getImageFilename();
        } else {
            return savePromoDto.getImageFilename();
        }
    }
}
