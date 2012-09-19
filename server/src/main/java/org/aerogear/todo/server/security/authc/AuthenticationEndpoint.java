package org.aerogear.todo.server.security.authc;

import org.jboss.picketlink.cdi.Identity;
import org.jboss.picketlink.cdi.credential.Credential;
import org.jboss.picketlink.cdi.credential.LoginCredentials;
import org.jboss.picketlink.idm.IdentityManager;
import org.jboss.picketlink.idm.model.Group;
import org.jboss.picketlink.idm.model.Role;
import org.jboss.picketlink.idm.model.User;
import org.picketbox.cdi.PicketBoxUser;
import org.picketbox.core.authentication.credential.UsernamePasswordCredential;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/auth")
@TransactionAttribute
public class AuthenticationEndpoint {

    @Inject
    private Identity identity;

    @Inject
    private LoginCredentials credential;

    @Inject
    private IdentityManager identityManager;

    /**
     * <p>Loads some users during the first construction.</p>
     */
    @PostConstruct
    public void loadUsers() {
        User john = this.identityManager.createUser("john");

        john.setEmail("john@doe.org");
        john.setFirstName("John");
        john.setLastName("Doe");

        this.identityManager.updatePassword(john, "123");

        Role roleDeveloper = this.identityManager.createRole("developer");
        Role roleAdmin = this.identityManager.createRole("admin");

        Group groupCoreDeveloper = identityManager.createGroup("Core Developers");

        identityManager.grantRole(roleDeveloper, john, groupCoreDeveloper);
        identityManager.grantRole(roleAdmin, john, groupCoreDeveloper);

        User guest = this.identityManager.createUser("guest");

        guest.setEmail("guest@aerogear.com");
        guest.setFirstName("Guest");
        guest.setLastName("User");

        this.identityManager.updatePassword(guest, "123");

        Role roleGuest = this.identityManager.createRole("guest");

        identityManager.grantRole(roleGuest, guest, groupCoreDeveloper);
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse register(User user) {
        System.out.println("User: " + user.getFirstName());
        return null;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse login(final AuthenticationRequest authcRequest) {

        System.out.println("LOGIN");
        if (this.identity.isLoggedIn()) {
            return createResponse(authcRequest);
        }

        credential.setUserId(authcRequest.getUserId());
        credential.setCredential(new Credential<UsernamePasswordCredential>() {

            @Override
            public UsernamePasswordCredential getValue() {
                return new UsernamePasswordCredential(authcRequest.getUserId(), authcRequest.getPassword());
            }
        });

        this.identity.login();

        return createResponse(authcRequest);
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void logout(final AuthenticationRequest authcRequest) {
        if (this.identity.isLoggedIn()) {
            this.identity.logout();
        }
    }

    private AuthenticationResponse createResponse(AuthenticationRequest authcRequest) {
        AuthenticationResponse response = new AuthenticationResponse();

        response.setUserId(authcRequest.getUserId());
        response.setLoggedIn(this.identity.isLoggedIn());

        if (response.isLoggedIn()) {
            PicketBoxUser user = (PicketBoxUser) this.identity.getUser();

            response.setToken(user.getSubject().getSession().getId().getId().toString());
        }

        return response;
    }
}
