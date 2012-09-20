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
package org.aerogear.todo.server.security.interceptor;

import org.aerogear.todo.server.security.authc.AuthenticationEndpoint;
import org.aerogear.todo.server.security.authc.AuthenticationResponse;
import org.apache.http.HttpStatus;
import org.jboss.logging.Logger;
import org.jboss.picketlink.cdi.authentication.AuthenticationException;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.picketbox.cdi.PicketBoxIdentity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * <p>
 * Implementation of {@link PreProcessInterceptor} that checks the existence of the authentication token before invoking the
 * destination endpoint.
 * </p>
 * <p>If the token is valid, the {@link PicketBoxIdentity} will restored with the all user information.</p>
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 */
@ApplicationScoped
@ServerInterceptor
public class SecurityInterceptor implements PreProcessInterceptor {

    private static final String AUTH_TOKEN_HEADER_NAME = "Auth-Token";

    private static final Logger LOGGER = Logger.getLogger(SecurityInterceptor.class);

    @Inject
    private PicketBoxIdentity identity;

    /* (non-Javadoc)
     * @see org.jboss.resteasy.spi.interception.PreProcessInterceptor#preProcess(org.jboss.resteasy.spi.HttpRequest, org.jboss.resteasy.core.ResourceMethod)
     */
    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
        ServerResponse response = null;

        try {
            if (requiresAuthentication(method)) {
                boolean isLoggedIn = false;
                String token = getToken(request);

                if (token != null) {
                    try {
                        isLoggedIn = identity.restoreSession(token);
                    } catch (AuthenticationException e) {
                        LOGGER.error("Fail: ", e);
                    }
                }

                if (!isLoggedIn) {
                    AuthenticationResponse authcResponse = new AuthenticationResponse();

                    authcResponse.setLoggedIn(false);

                    response = new ServerResponse();
                    response.setEntity(authcResponse);
                    //TODO won' work when you need public pages like registration page, must to be fixed or moved
                    response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Fail: ", e);
        }

        return response;
    }

    /**
     * <p>Retrieve the token from the request, if present.</p>
     *
     * @param request
     * @return
     */
    private String getToken(HttpRequest request) {
        List<String> tokenHeader = request.getHttpHeaders().getRequestHeader(AUTH_TOKEN_HEADER_NAME);
        String token = null;

        if (tokenHeader!= null && !tokenHeader.isEmpty()) {
            token = tokenHeader.get(0);
        }

        return token;
    }

    /**
     * <p>Checks if the {@link ResourceMethod} requires authentication.</p>
     *
     * @param method
     * @return
     */
    //TODO must be removed
    private boolean requiresAuthentication(ResourceMethod method) {
        return !method.getMethod().getDeclaringClass().equals(AuthenticationEndpoint.class);
    }

}
