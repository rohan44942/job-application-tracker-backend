package com.jobtracker.service;

import com.jobtracker.entity.User;
import com.jobtracker.exception.UnauthorizedException;
import com.jobtracker.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails userDetails)) {
            throw new UnauthorizedException("Authentication required");
        }
        return userDetails.user();
    }
}
