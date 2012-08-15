package org.aerogear.todo.server.security.idm;

import org.apache.deltaspike.security.api.credential.Credential;
import org.picketbox.core.authentication.credential.UsernamePasswordCredential;

public class SimpleCredential implements Credential<UsernamePasswordCredential> {

    private String name;
    private String password;

    public SimpleCredential(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public UsernamePasswordCredential getValue() {
        return new UsernamePasswordCredential(name, password);
    }
}
