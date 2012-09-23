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

package org.aerogear.todo.server.util;

import org.aerogear.todo.server.security.idm.AeroGearCredential;
import org.aerogear.todo.server.security.idm.AeroGearUser;
import org.jboss.picketlink.cdi.Identity;
import org.picketbox.cdi.PicketBoxUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HttpResponseBuilder {

    @Inject
    private Identity identity;

    public AeroGearCredential createResponse(String username) {
        AeroGearCredential response = new AeroGearCredential();

        response.setLogged(identity.isLoggedIn());

        if (response.isLogged()) {
            PicketBoxUser user = (PicketBoxUser) identity.getUser();

            response.setToken(user.getSubject().getSession().getId().getId().toString());
        }

        return response;
    }
}
