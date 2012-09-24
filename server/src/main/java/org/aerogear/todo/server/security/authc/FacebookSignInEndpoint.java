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
package org.aerogear.todo.server.security.authc;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.aerogear.todo.server.security.authc.social.fb.FacebookCredential;
import org.aerogear.todo.server.security.authc.social.fb.FacebookPrincipal;
import org.jboss.picketlink.cdi.Identity;
import org.jboss.picketlink.cdi.credential.Credential;
import org.jboss.picketlink.cdi.credential.LoginCredentials;
import org.jboss.picketlink.idm.IdentityManager;
import org.jboss.picketlink.idm.model.Group;
import org.jboss.picketlink.idm.model.Role;
import org.jboss.picketlink.idm.model.User;
import org.picketbox.cdi.PicketBoxCDISubject;
import org.picketbox.cdi.PicketBoxUser;
import org.picketbox.core.PicketBoxSubject;

/**
 * Enables signin with facebook
 * 
 * @author anil saldhana
 * @since Sep 19, 2012
 */
@Stateless
@Path("/facebook")
public class FacebookSignInEndpoint {

    @Inject
    private Identity identity;

    @Inject
    private LoginCredentials credential;

    @Inject
    private IdentityManager identityManager;

    @GET
    public String login(@Context final HttpServletRequest request, @Context final HttpServletResponse response) throws IOException {
        if (this.identity.isLoggedIn()) {
            return null;
        }
        
        this.credential.setCredential(new Credential<FacebookCredential>() {

            @Override
            public FacebookCredential getValue() {
                return new FacebookCredential(request, response);
            }
        });
        
        this.identity.login();
        
        if (this.identity.isLoggedIn()) {
            provisionNewUser();
            return "<script>window.opener.sendMainPage();</script>";
        }
        
        return null;
    }

    /**
     * <p>Provision the authenticated user if he is not stored yes.</p>
     * 
     * TODO: user provisioning feature should be provided by PicketBox ? 
     */
    private void provisionNewUser() {
        FacebookPrincipal principal = getAuthenticatedPrincipal();
        
        //Check if the user exists in DB
        User storedUser = identityManager.getUser(principal.getName());
        ArrayList<String> roles = new ArrayList<String>();
        
        if(storedUser == null) {
            storedUser = identityManager.createUser(principal.getEmail());
            storedUser.setFirstName(principal.getFirstName());
            storedUser.setLastName(principal.getLastName());

            Role guest = this.identityManager.createRole("guest");
            
            roles.add(guest.getName());
            
            Group guests = identityManager.createGroup("Guests");

            identityManager.grantRole(guest, storedUser, guests);
            
            // necessary because we need to show the user info at the main page. Otherwise the informations will be show only after the second login.
            PicketBoxUser user = (PicketBoxUser) identity.getUser();
            PicketBoxCDISubject subject = user.getSubject();
            
            subject.setIdmUser(storedUser);
            
            subject.setRoleNames(roles);
        }
    }

    private FacebookPrincipal getAuthenticatedPrincipal() {
        PicketBoxUser user = (PicketBoxUser) identity.getUser();
        PicketBoxSubject subject = user.getSubject();
        
        return (FacebookPrincipal) subject.getUser();
    }

    private AuthenticationResponse createSuccessfulAuthResponse() {
        AuthenticationResponse response = new AuthenticationResponse();
        
        response.setLoggedIn(this.identity.isLoggedIn());
        
        return response;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse getStatus(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException{
        if(identity.isLoggedIn()){
            User user = identity.getUser();
            AuthenticationResponse authResponse = createSuccessfulAuthResponse();
            authResponse.setLoggedIn(true);
            authResponse.setToken(user.getId());
            return authResponse;
        }
        return null;
    }
}