package org.sohan.BookManagementSystem.config;

import org.sohan.BookManagementSystem.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//Type of User ID is integer

public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override

    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !(authentication.isAuthenticated())
        || authentication instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }
        return Optional.ofNullable(((User)authentication.getPrincipal()).getId());

    }
}
