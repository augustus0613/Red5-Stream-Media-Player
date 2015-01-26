/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjm.user.dao;

import java.util.Set;
import javax.ejb.Local;
import org.tjm.user.entity.Authority;
import org.tjm.user.entity.User;

/**
 *
 * @author larry
 */
@Local
public interface UserDao {

    public User selectUserByUsername(String username);

    public Set<Authority> selectAuthoritySetByUserId(Long userId);
}
