package pl.dk.dealspotter.user.dto;

import pl.dk.dealspotter.promo.Promo;

import java.util.List;

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<Promo> promo;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<Promo> getPromo() {
        return promo;
    }

    public void setPromo(List<Promo> promo) {
        this.promo = promo;
    }
}
