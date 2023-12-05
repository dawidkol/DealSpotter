package pl.dk.dealspotter.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.dk.dealspotter.promo.Promo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "application_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 5)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<UserRole> roles = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Promo> promo;

}
