/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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