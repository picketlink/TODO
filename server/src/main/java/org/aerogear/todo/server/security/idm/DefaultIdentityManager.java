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

package org.aerogear.todo.server.security.idm;

import org.picketbox.core.PicketBoxSubject;
import org.picketbox.core.identity.IdentityManager;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DefaultIdentityManager implements IdentityManager {

    @Override
    public PicketBoxSubject getIdentity(PicketBoxSubject picketBoxSubject) {
        List<String> roles = new ArrayList<String>();

        if (picketBoxSubject.getUser().getName().equals("admin")) {
            roles.add("admin");
        } else {
            roles.add("guest");
        }

        picketBoxSubject.setRoleNames(roles);

        return picketBoxSubject;
    }

}
