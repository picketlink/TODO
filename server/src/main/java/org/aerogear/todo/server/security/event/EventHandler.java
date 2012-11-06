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

package org.aerogear.todo.server.security.event;

import java.security.Principal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.picketbox.core.UserContext;
import org.picketbox.core.authentication.AuthenticationResult;
import org.picketbox.core.authentication.AuthenticationStatus;
import org.picketbox.core.authentication.event.UserAuthenticatedEvent;

/**
 * <p>
 * Simple bean that observes for some security events.
 * </p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 * 
 */
@ApplicationScoped
public class EventHandler {

    /**
     * <p>
     * Method that observes PicketBox {@link AuthenticationEvent} events. An event will be raised for any successfull
     * authentication performed by the registered {@link AuthenticationManager} instances.
     * </p>
     * 
     * @param event
     */
    public void onUserAuthenticatedEvent(@Observes UserAuthenticatedEvent event) {
        UserContext subject = event.getUserContext();
        AuthenticationResult authenticationResult = subject.getAuthenticationResult();
        AuthenticationStatus status = authenticationResult.getStatus();

        // list of messages gathered during the authentication
        List<String> messages = authenticationResult.getMessages();

        if (status.equals(AuthenticationStatus.SUCCESS)) {
            Principal principal = authenticationResult.getPrincipal();
            // handle a successfull authentication
        } else if (status.equals(AuthenticationStatus.FAILED)) {
            // handle a failed authentication
        }
    }
}
