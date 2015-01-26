/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.user.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.tjm.user.dao.UserDao;
import org.tjm.user.entity.AuthGroup;
import org.tjm.user.entity.AuthGroupAuthorityMap;
import org.tjm.user.entity.Authority;
import org.tjm.user.entity.User;
import org.tjm.user.entity.UserAuthGroupMap;

/**
 *
 * @author larry
 */
@Stateless
public class UserDaoImpl implements UserDao {

    @PersistenceContext(unitName = "tjm")
    public EntityManager em;

    @Override
    public User selectUserByUsername(String username) {

        Query query = em.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username", username);
        List<User> userList = query.getResultList();
        if (userList.isEmpty()) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public Set<Authority> selectAuthoritySetByUserId(Long userId) {

        List<AuthGroup> groupList = em.createQuery("SELECT uagm.authGroup FROM UserAuthGroupMap uagm WHERE uagm.user.id=:userId").setParameter("userId", userId).getResultList();
        List<Long> groupIdList = new ArrayList<Long>();
        List<Authority> authorityList = new ArrayList<Authority>();
        for (AuthGroup group : groupList) {
            groupIdList.add(group.getId());
        }
        if (!groupIdList.isEmpty()) {
            authorityList = em.createQuery("SELECT agam.authority FROM AuthGroupAuthorityMap agam WHERE agam.authGroup.id IN :groupIdList").setParameter("groupIdList", groupIdList).getResultList();
        }
        Set<Authority> authSet = new LinkedHashSet<Authority>(authorityList);
        return authSet;
    }
}
