package org.aerogear.todo.server.security.exception;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.deltaspike.security.api.authentication.AuthenticationException;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<Throwable> {

    @Inject
    private Event<ExceptionToCatchEvent> event;

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof AuthenticationException) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build();
        }
        return Response.noContent().build();
    }

}