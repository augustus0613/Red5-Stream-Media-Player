package org.tjm.user.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.tjm.user.entity.AuthGroup;
import org.tjm.user.entity.User;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2015-01-27T02:47:57")
@StaticMetamodel(UserAuthGroupMap.class)
public class UserAuthGroupMap_ { 

    public static volatile SingularAttribute<UserAuthGroupMap, Long> id;
    public static volatile SingularAttribute<UserAuthGroupMap, AuthGroup> authGroup;
    public static volatile SingularAttribute<UserAuthGroupMap, User> user;

}