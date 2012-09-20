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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.aerogear.todo.server.security.authc.fb.FacebookProcessor;
import org.jboss.picketlink.cdi.Identity;

/**
 * Enables signin with facebook
 * @author anil saldhana
 * @since Sep 19, 2012
 */
@Stateless
@Path("/facebook")
public class FacebookSignInEndpoint {

    @Resource
    ServletContext context;
    
    @Inject
    private Identity identity;
  

    private enum STATES {
        AUTH, AUTHZ, FINISH
    };

    protected String returnURL;
    protected String clientID;
    protected String clientSecret;
    protected String scope = "email";

    protected List<String> roles = new ArrayList<String>();
    protected FacebookProcessor processor;
    
    public FacebookSignInEndpoint(){
        clientID = System.getProperty("FB_CLIENT_ID");
        clientSecret = System.getProperty("FB_CLIENT_SECRET");
        returnURL = System.getProperty("FB_RETURN_URL");
        roles.add("guest");
    }
    
    
    @GET
    public boolean login(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        String state = (String) session.getAttribute("STATE");
        if (processor == null)
            processor = new FacebookProcessor(clientID, clientSecret, scope, returnURL, roles);

        if (STATES.FINISH.name().equals(state)){
            return true;
        }
        
        if (state == null || state.isEmpty()) {
            /*if (saveRestoreRequest) {
                this.saveRequest(request, request.getSessionInternal());
            }*/
            return  processor.initialInteraction(request, response);
        }
        // We have sent an auth request
        if (state.equals(STATES.AUTH.name())) {
            return  processor.handleAuthStage(request, response);
        }

        // Principal facebookPrincipal = null;
        if (state.equals(STATES.AUTHZ.name())) {
            Principal principal = processor.getPrincipal(request, response);

            if (principal == null) {
                //log.error("Principal was null. Maybe login modules need to be configured properly.");
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
             
            session.setAttribute("PRINCIPAL", principal);
 
            session.setAttribute("STATE", STATES.FINISH.name());            

            return true;
        }
        return false;
    }
    
    private String getUserName(HttpServletRequest request){
        HttpSession session = request.getSession();
        Principal principal = (Principal) session.getAttribute("PRINCIPAL");
        if(principal != null){
            return principal.getName();
        }
        return null;
    }
    
    private AuthenticationResponse success(HttpServletRequest request){
        AuthenticationResponse response = new AuthenticationResponse();
        response.setLoggedIn(true);
        response.setToken(getUserName(request));
        return response;
    }
}