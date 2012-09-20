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
package org.aerogear.todo.server.security.event;

import java.security.Principal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;
import org.picketbox.core.authentication.AuthenticationManager;
import org.picketbox.core.authentication.AuthenticationStatus;
import org.picketbox.core.authentication.event.AuthenticationEvent;
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

    private static final Logger LOGGER = Logger.getLogger(EventHandler.class);

    /**
     * <p>
     * Method that observes PicketBox {@link AuthenticationEvent} events.
     * </p>
     * 
     * @param event
     */
    @SuppressWarnings("rawtypes")
    public void onAnyAuthenticationEvent(@Observes AuthenticationEvent event) {
        LOGGER.debug("Yay, the user was authenticated and onAnyAuthenticationEvent was triggered");
    }

    /**
     * <p>
     * Method that observes PicketBox {@link AuthenticationEvent} events. An event will be raised for any successfull
     * authentication performed by the registered {@link AuthenticationManager} instances.
     * </p>
     * 
     * @param event
     */
    public void onUserAuthenticatedEvent(@Observes UserAuthenticatedEvent event) {

        LOGGER.debug("Yay, the user was authenticated and onUserAuthenticatedEvent was triggered");

        AuthenticationStatus status = event.getResult().getStatus();

        // list of messages gathered during the authentication
        List<String> messages = event.getResult().getMessages();

        if (status.equals(AuthenticationStatus.SUCCESS)) {
            Principal principal = event.getResult().getPrincipal();
            // handle a successfull authentication
        } else if (status.equals(AuthenticationStatus.FAILED)) {
            // handle a failed authentication
        }
    }

}
