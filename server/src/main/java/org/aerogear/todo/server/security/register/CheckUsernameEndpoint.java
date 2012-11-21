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

package org.aerogear.todo.server.security.register;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.User;

/**
 * <p>
 * JAX-RS Endpoint that checks if an username is already use.
 * </p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 * 
 */
@Stateless
@Path("/checkUsername")
public class CheckUsernameEndpoint {

    @Inject
    private IdentityManager identityManager;

    /**
     * <p>
     * Creates a new user using the information provided by the {@link RegistrationRequest} instance.
     * </p>
     * 
     * @param request
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegistrationResponse checkUsername(final RegistrationRequest request) {
        RegistrationResponse response = new RegistrationResponse();
        
        if (!"".equals(request.getUserName())) {
            User user = this.identityManager.getUser(request.getUserName());
            
            if (user != null) {
                response.setStatus("This username is already in use. Choose another one.");
            }
        } 
        
        return response;
    }
    
    /**
     * <p>Checks if the provided informations are valid.</p>
     * 
     * @param request
     * @return
     */
    public boolean validateUserInformation(RegistrationRequest request) {
        String username = request.getUserName();
        String password = request.getPassword();
        String email = request.getEmail();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        return !("".equals(username.trim()) || "".equals(password.trim()) || "".equals(firstName.trim())
                || "".equals(lastName.trim()) || "".equals(email.trim()));
    }
}