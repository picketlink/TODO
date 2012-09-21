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
package org.aerogear.todo.server.security.service;

import org.aerogear.todo.server.security.authc.AuthenticationRequest;
import org.aerogear.todo.server.security.authc.AuthenticationResponse;
import org.aerogear.todo.server.util.PasswordHashing;
import org.jboss.logging.Logger;
import org.jboss.picketlink.cdi.Identity;
import org.jboss.picketlink.cdi.credential.LoginCredentials;
import org.jboss.picketlink.idm.IdentityManager;
import org.jboss.picketlink.idm.model.Group;
import org.jboss.picketlink.idm.model.Role;
import org.jboss.picketlink.idm.model.User;
import org.picketbox.cdi.PicketBoxUser;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
public class UserManager {

    @Inject
    private LoginCredentials credential;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private Identity identity;

    private static final Logger LOGGER = Logger.getLogger(UserManager.class);

    public AuthenticationResponse createResponse(String username) {
        AuthenticationResponse response = new AuthenticationResponse();

        response.setUserId(username);
        response.setLoggedIn(identity.isLoggedIn());

        if (response.isLoggedIn()) {
            PicketBoxUser user = (PicketBoxUser) identity.getUser();

            response.setToken(user.getSubject().getSession().getId().getId().toString());
        }

        return response;
    }

    //TODO point of huge refactoring here
    //TODO Make use of entities instead of DTOs
    public void registerUser(AuthenticationRequest authenticationRequest) {
        User user = identityManager.createUser(authenticationRequest.getUserId());

        user.setEmail(authenticationRequest.getEmail());
        user.setFirstName(authenticationRequest.getFirstName());
        user.setLastName(authenticationRequest.getLastName());

        //TODO must be encrypted
        identityManager.updatePassword(user, authenticationRequest.getPassword());

        Role roleDeveloper = identityManager.createRole("developer");
        Role roleAdmin = identityManager.createRole("admin");

        Group groupCoreDeveloper = identityManager.createGroup("Core Developers");

        identityManager.grantRole(roleDeveloper, user, groupCoreDeveloper);
        identityManager.grantRole(roleAdmin, user, groupCoreDeveloper);

        //TODO to be refactored
        authenticationManager.login(authenticationRequest.getUserId(), authenticationRequest.getPassword());
    }


    /**
     * <p>Loads some users during the first construction.</p>
     */
    @PostConstruct
    public void loadUsers() {
        User john = this.identityManager.createUser("john");

        john.setEmail("john@doe.org");
        john.setFirstName("John");
        john.setLastName("Doe");

        identityManager.updatePassword(john, PasswordHashing.digest("123"));

        Role roleDeveloper = identityManager.createRole("developer");
        Role roleAdmin = identityManager.createRole("admin");

        Group groupCoreDeveloper = identityManager.createGroup("Core Developers");

        identityManager.grantRole(roleDeveloper, john, groupCoreDeveloper);
        identityManager.grantRole(roleAdmin, john, groupCoreDeveloper);

    }


}
