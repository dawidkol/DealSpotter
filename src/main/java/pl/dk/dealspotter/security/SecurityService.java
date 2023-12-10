package pl.dk.dealspotter.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public static String findCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
