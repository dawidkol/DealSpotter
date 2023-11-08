package pl.dk.dealspotter.promo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavePromoDto {
    private Long id;
    @NotNull
    @Size(min = 2, max = 100)
    private String name;
    @NotNull
    @Size(min = 10, max = 20000)
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    @NotNull
    @URL
    private String urlAddress;
    private String category;
    private String imageFilename;
}
