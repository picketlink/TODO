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

package org.aerogear.todo.server.security;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.aerogear.todo.server.security.authc.social.fb.FacebookAuthenticationMechanism;
import org.aerogear.todo.server.security.authc.social.openid.OpenIDAuthenticationMechanism;
import org.aerogear.todo.server.security.authc.social.twitter.TwitterAuthenticationMechanism;
import org.jboss.picketlink.idm.IdentityManager;
import org.jboss.picketlink.idm.internal.JPAIdentityStore;
import org.jboss.picketlink.idm.internal.jpa.JPATemplate;
import org.picketbox.cdi.config.CDIConfigurationBuilder;
import org.picketbox.core.config.ConfigurationBuilder;

/**
 * <p>Bean responsible for producing the {@link CDIConfigurationBuilder}.</p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
public class PicketBoxConfigurer {

    @Inject
    private EntityManager entityManager;

    @Inject
    private BeanManager beanManager;

    @Inject
    private FacebookAuthenticationMechanism fbAuthenticationMechanism;
    

    @Inject
    private OpenIDAuthenticationMechanism openidAuthenticationMechanism;
    

    @Inject
    private TwitterAuthenticationMechanism twitterAuthenticationMechanism;
    
    @Inject
    private IdentityManager identityManager;
    
    
    /**
     * <p>Produces the {@link ConfigurationBuilder}.</p>
     * 
     * @return
     */
    @Produces
    public ConfigurationBuilder produceConfiguration() {
        fbAuthenticationMechanism.setIdentityManager(identityManager);
        openidAuthenticationMechanism.setIdentityManager(identityManager);
        twitterAuthenticationMechanism.setIdentityManager(identityManager);
        
        CDIConfigurationBuilder builder = new CDIConfigurationBuilder(this.beanManager);

        builder.authentication().mechanism(this.fbAuthenticationMechanism).mechanism(openidAuthenticationMechanism)
        .mechanism(twitterAuthenticationMechanism);
        
        builder
            .authentication()
                .idmAuthentication()
            .identityManager()
                .providedStore()
            .sessionManager()
                .inMemorySessionStore();
        
        return builder;
    }
    
    /**
     * <p>Produces the {@link JPAIdentityStore} that will be used by the PicketLink IDM {@link IdentityManager}.</p>
     * 
     * @return
     */
    @Produces
    public JPAIdentityStore produceIdentityStore() {
        JPAIdentityStore identityStore = new JPAIdentityStore();

        JPATemplate template = new JPATemplate();

        template.setEntityManager(this.entityManager);

        identityStore.setJpaTemplate(template);

        return identityStore;
    }
    
}