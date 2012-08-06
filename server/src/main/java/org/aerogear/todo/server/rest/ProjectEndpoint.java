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
package org.aerogear.todo.server.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.ws.rs.*;
import org.aerogear.todo.server.model.Project;

/**
 * 
 */
@Stateful
@Path("/project")
@TransactionAttribute
public class ProjectEndpoint
{
   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Project create(Project entity)
   {
      em.joinTransaction();
      em.persist(entity);
      return entity;
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Project deleteById(@PathParam("id")
   Long id)
   {
      em.joinTransaction();
      Project result = em.find(Project.class, id);
      em.remove(result);
      return result;
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Project findById(@PathParam("id")
   Long id)
   {
      return em.find(Project.class, id);
   }

   @GET
   @Produces("application/json")
   public List<Project> listAll()
   {
      @SuppressWarnings("unchecked")
      final List<Project> results = em.createQuery("SELECT x FROM Project x").getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Project update(@PathParam("id")
   Long id, Project entity)
   {
      entity.setId(id);
      em.joinTransaction();
      entity = em.merge(entity);
      return entity;
   }
}