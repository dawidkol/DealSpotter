package pl.dk.dealspotter.promo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.dk.dealspotter.user.dto.UserDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PromoDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String urlAddress;
    private String category;
    private String imageFilename;
    private UserDto userDto;
    private LocalDateTime localDateTime;
}
