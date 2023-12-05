package pl.dk.dealspotter.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "user_role")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 3)
    private String name;
    @NotNull
    @Size(min = 3)
    private String description;
}
