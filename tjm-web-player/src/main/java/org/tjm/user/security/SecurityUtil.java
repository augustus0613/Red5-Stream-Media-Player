package org.tjm.user.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author larry
 */
public class SecurityUtil {

    public static UserDetails getUserDetails() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof UserDetails) {
            return (UserDetails) object;
        } else {
            return null;
        }
    }

    public static String getUsername() {
        UserDetails details = SecurityUtil.getUserDetails();
        if (details != null) {
            return details.getUsername();
        } else {
            return null;
        }
    }
}
