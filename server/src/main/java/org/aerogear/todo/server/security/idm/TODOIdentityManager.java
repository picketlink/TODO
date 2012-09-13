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

import javax.enterprise.context.ApplicationScoped;

import org.jboss.picketlink.idm.internal.DefaultIdentityManager;
import org.jboss.picketlink.idm.model.User;
import org.picketbox.cdi.idm.IdentityManagerImpl;
import org.picketbox.core.PicketBoxSubject;

/**
 * <p>Customizes the {@link IdentityManagerImpl} to load some users during the first call.</p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@ApplicationScoped
public class TODOIdentityManager extends IdentityManagerImpl {

    private boolean initialized;
    
    @Override
    public PicketBoxSubject getIdentity(PicketBoxSubject resultingSubject) {
        if (!initialized) {
            DefaultIdentityManager identityManager = getIdentityManager();
            
            User user = identityManager.createUser("admin");
            
            user.setEmail("admin@admin.com.br");
            user.setFirstName("The");
            user.setLastName("Administrator");
            
            this.initialized = true;
        }
        
        return super.getIdentity(resultingSubject);
    }
    
}
