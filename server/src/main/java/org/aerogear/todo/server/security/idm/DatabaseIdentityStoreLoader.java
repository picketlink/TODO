/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.aerogear.todo.server.security.idm;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.picketbox.core.identity.impl.JPAIdentityStoreContext;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.PasswordCredential;
import org.picketlink.idm.model.Group;
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.User;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@Singleton
@Startup
public class DatabaseIdentityStoreLoader {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    
    @Inject
    private IdentityManager identityManager;
    
    /**
     * <p>Loads some users during the first construction.</p>
     */
    //TODO this entire initialization code will be removed
    @PostConstruct
    public void create() {
        JPAIdentityStoreContext.set(this.entityManager);
        
        User abstractj = this.identityManager.createUser("abstractj");

        abstractj.setEmail("abstractj@aerogear.com");
        abstractj.setFirstName("Bruno");
        abstractj.setLastName("Oliveira");
        
        this.identityManager.updateCredential(abstractj, new PasswordCredential("123"));
        
        Role roleDeveloper = this.identityManager.createRole("developer");
        Role roleAdmin = this.identityManager.createRole("admin");

        Group groupCoreDeveloper = identityManager.createGroup("Core Developers");

        identityManager.grantRole(roleDeveloper, abstractj, groupCoreDeveloper);
        identityManager.grantRole(roleAdmin, abstractj, groupCoreDeveloper);
        
        User guest = this.identityManager.createUser("guest");

        guest.setEmail("guest@aerogear.com");
        guest.setFirstName("Guest");
        guest.setLastName("User");

        this.identityManager.updateCredential(guest, new PasswordCredential("123"));
        
        Role roleGuest = this.identityManager.createRole("guest");
        
        identityManager.grantRole(roleGuest, guest, groupCoreDeveloper);
        
        JPAIdentityStoreContext.clear();
    }

}
