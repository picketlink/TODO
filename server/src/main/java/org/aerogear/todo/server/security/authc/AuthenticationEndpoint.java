package org.aerogear.todo.server.security.authc;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/auth")
@TransactionAttribute
public class AuthenticationEndpoint {

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse register(AuthenticationRequest authenticationRequest) {
        return null;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse login(final AuthenticationRequest authcRequest) {
        return null;
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse logout(final AuthenticationRequest authcRequest) {
        return null;
    }
}
