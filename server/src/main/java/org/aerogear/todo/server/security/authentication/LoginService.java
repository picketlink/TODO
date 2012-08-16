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

package org.aerogear.todo.server.security.authentication;

import org.aerogear.todo.server.security.idm.SimpleCredential;
import org.apache.deltaspike.security.api.Identity;
import org.apache.deltaspike.security.api.Identity.AuthenticationResult;
import org.apache.deltaspike.security.api.credential.Credential;
import org.apache.deltaspike.security.api.credential.LoginCredential;
import org.picketbox.core.authentication.credential.UsernamePasswordCredential;

import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/auth")
public class LoginService {

    @Inject
    private LoginCredential credential;

    @Inject
    private Identity identity;

    @POST
    @Path("/login")
    @Produces("application/json")
    public Response login(final @HeaderParam("Auth-Credential") String name,
                          final @HeaderParam("Auth-Password") String password) {

        if (this.identity.isLoggedIn()) {
            return Response.ok(AuthenticationResult.SUCCESS).build();
        }

        credential.setUserId(name);
        credential.setCredential(new SimpleCredential(name, password));

        AuthenticationResult result = this.identity.login();

        return Response.ok(createResponse(result)).build();
    }

    private String createResponse(AuthenticationResult result) {
        return "{\"authentication\": {\"loggedIn\":" + result.equals(AuthenticationResult.SUCCESS) + "\"}}";
    }
}
