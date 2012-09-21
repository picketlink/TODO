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
package org.aerogear.todo.server.security.idm;

import org.jboss.picketlink.cdi.Identity;
import org.picketbox.cdi.PicketBoxUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * <p>JAX-RS Endpoint to authenticate users.</p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 */
@Stateless
@Path("/userinfo")
public class UserInfoEndpoint {

    @Inject
    private Identity identity;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getInfo() {
        UserInfo userInfo = new UserInfo();
        
        PicketBoxUser user = (PicketBoxUser) this.identity.getUser();
        
        userInfo.setUserId(user.getSubject().getUser().getName());
        userInfo.setFullName(user.getFullName());
        
        List<String> roles = user.getSubject().getRoleNames();
        
        userInfo.setRoles(roles.toArray(new String[roles.size()]));
        
        return userInfo;
    }
    
}