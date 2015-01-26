package org.tjm.user.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.tjm.user.entity.AuthGroup;
import org.tjm.user.entity.Authority;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2015-01-27T02:47:57")
@StaticMetamodel(AuthGroupAuthorityMap.class)
public class AuthGroupAuthorityMap_ { 

    public static volatile SingularAttribute<AuthGroupAuthorityMap, Long> id;
    public static volatile SingularAttribute<AuthGroupAuthorityMap, Authority> authority;
    public static volatile SingularAttribute<AuthGroupAuthorityMap, AuthGroup> authGroup;

}