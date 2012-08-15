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

package org.aerogear.todo.server.security.authentication;

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
        credential.setCredential(new Credential<UsernamePasswordCredential>() {

            @Override
            public UsernamePasswordCredential getValue() {
                return new UsernamePasswordCredential(name, password);
            }
        });

        AuthenticationResult result = this.identity.login();

        return Response.ok(createResponse(result)).build();
    }

    private String createResponse(AuthenticationResult result) {
        return "{\"authentication\": {\"loggedIn\":" + result.equals(AuthenticationResult.SUCCESS) + "\"}}";
    }
}
