/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.user.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.tjm.user.dao.UserDao;
import org.tjm.user.entity.Authority;
import org.tjm.user.entity.User;

/**
 *
 * @author larry
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource(name = "userDao")
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userDao.selectUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not founed !");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        if (!user.isDeleted() && user.isEnabled()) {
            // Set 儲存使用者具有的權限 ( Set 中的物件不會重複 )
            Set<Authority> authoritySet = userDao.selectAuthoritySetByUserId(user.getId());
            for (Authority authority : authoritySet) {
                System.out.println("authority = " + authority.getName());
                grantedAuthorities.add(new GrantedAuthorityImpl(authority.getName()));
            }
        }

        org.springframework.security.core.userdetails.User details = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, grantedAuthorities);

        return details;

    }

}
