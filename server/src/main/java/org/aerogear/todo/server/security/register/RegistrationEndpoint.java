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
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.User;

/**
 * <p>JAX-RS Endpoint to register users.</p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@Stateless
@Path("/register")
public class RegistrationEndpoint {
    
    @Inject
    private IdentityManager identityManager;
    
    /**
     * <p>Performs the authentication using the information provided by the {@link RegistrationRequest}</p>
     * 
     * @param request
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegistrationResponse login(final RegistrationRequest request) {
       
        String username = request.getUserName();
        String password = request.getPassword();
        String email = request.getEmail();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String address = request.getAddress();
        String city = request.getCity();
        String postalCode = request.getPostalCode();
        String country = request.getCountry();
        String state = request.getState();
        
        User user = identityManager.createUser(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAttribute("city", city);
        user.setAttribute("postalCode", postalCode);

        user.setAttribute("address", address);
        user.setAttribute("state", state);
        user.setAttribute("country", country);
        
        identityManager.updatePassword(user, password);
        
        Role roleGuest = this.identityManager.createRole("guest");
        
        identityManager.grantRole(roleGuest, user, null);
        
        RegistrationResponse response = new RegistrationResponse();
        response.setStatus("Success");

        return response;
    }
}