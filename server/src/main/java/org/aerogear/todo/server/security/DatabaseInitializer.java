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
