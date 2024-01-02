package pl.dk.dealspotter.promo;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import pl.dk.dealspotter.category.Category;
import pl.dk.dealspotter.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Size(min = 10)
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    @URL
    private String urlAddress;
    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;
    private String imageFilename;
    @NotNull
    @ManyToOne(targetEntity = User.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    @PastOrPresent
    private LocalDateTime added;
}
