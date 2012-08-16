/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aerogear.todo.server.security;

import org.picketbox.core.config.ConfigurationBuilder;
import org.picketbox.core.config.PicketBoxConfiguration;
import org.picketbox.core.identity.IdentityManager;
import org.picketbox.deltaspike.event.CDIAuthenticationEventManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@ApplicationScoped
public class PicketBoxConfigurer {

    @Inject
    private BeanManager beanManager;

    @Inject
    private IdentityManager identityManager;

    @Produces
    public PicketBoxConfiguration createConfiguration() {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder
                .authentication()
                .dataBaseAuthManager().jpaJndiName("java:/AuthTestEMF").passwordQuery("SELECT PASSWORD FROM PICKETBOX_USERS WHERE USERNAME = ?")
                .eventManager().manager(new CDIAuthenticationEventManager(this.beanManager))
                .identityManager()
                .manager(this.identityManager);

        return builder.build();
    }

}
