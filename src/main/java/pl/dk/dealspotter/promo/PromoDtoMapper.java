package pl.dk.dealspotter.promo;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.dk.dealspotter.category.Category;
import pl.dk.dealspotter.category.CategoryRepository;
import pl.dk.dealspotter.user.User;
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
        return PromoDto.builder()
                .id(promo.getId())
                .name(promo.getName())
                .description(promo.getDescription())
                .price(promo.getPrice())
                .urlAddress(promo.getUrlAddress())
                .category(promo.getCategory().getName())
                .imageFilename(promo.getImageFilename())
                .userDto(getUserDto(promo))
                .localDateTime(promo.getLocalDateTime())
                .build();
    }

    private UserDto getUserDto(Promo promo) {
        return userRepository.findById(promo.getUser().getId()).map(userDtoMapper::map).orElseThrow(NoSuchElementException::new);
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
                .user(getCurrentUser())
                .localDateTime(LocalDateTime.now())
                .build();
    }

    private Category getCategory(SavePromoDto savePromoDto) {
        return categoryRepository.findByName(savePromoDto.getCategory()).orElseThrow(NoSuchElementException::new);
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(NoSuchElementException::new);
    }

    private String setImage (SavePromoDto savePromoDto) {
        if (savePromoDto.getImageFilename().isEmpty()) {
            return savePromoDto.getImageFilename();
        } else {
            return savePromoDto.getImageFilename();
        }
    }
}
