package org.aerogear.todo.server.security.authc;

import org.jboss.picketlink.idm.IdentityManager;
import org.jboss.picketlink.idm.model.Group;
import org.jboss.picketlink.idm.model.Role;
import org.jboss.picketlink.idm.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
@DependsOn("SignInEndpoint")
public class ConfigurationInitializer {

    @Inject
    private IdentityManager identityManager;

    /**
     * <p>Loads some users during the first construction.</p>
     */
    @PostConstruct
    public void loadUsers() {

        System.out.println("INITIALIZING IT!");
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
}
