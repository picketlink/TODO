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

package org.aerogear.todo.server.security;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Startup
@Singleton
public class DatabaseInitializer {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @PostConstruct
    public void onStartup() {
        try {

            Query dropUserTableStm = entityManager.createNativeQuery("DROP TABLE IF EXISTS PICKETBOX_USERS;");
            Query createUserTableStm = entityManager.createNativeQuery("CREATE TABLE PICKETBOX_USERS(username varchar2(20) not null, password varchar2(20) not null);");
            Query loadAdminUserStm = entityManager.createNativeQuery("INSERT INTO PICKETBOX_USERS(username, password) VALUES ('admin', 'admin');");
            Query loadGuestUserStm = entityManager.createNativeQuery("INSERT INTO PICKETBOX_USERS(username, password) VALUES ('guest', 'guest');");

            dropUserTableStm.executeUpdate();
            createUserTableStm.executeUpdate();
            loadAdminUserStm.executeUpdate();
            loadGuestUserStm.executeUpdate();

        } catch (Exception e) {
            throw new IllegalStateException("Error initializing database.", e);
        }
    }
}
