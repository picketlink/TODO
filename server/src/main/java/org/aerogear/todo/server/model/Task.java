package org.aerogear.todo.server.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Lob;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Task implements java.io.Serializable
{

   @Id
   private @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   Long id = null;
   @Version
   private @Column(name = "version")
   int version = 0;

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((Task) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   @Column
   private String title;

   public String getTitle()
   {
      return this.title;
   }

   public void setTitle(final String title)
   {
      this.title = title;
   }

   @Lob
   @Column(name="description", columnDefinition="TEXT")
   private String description;

   public String getDescription()
   {
      return this.description;
   }

   public void setDescription(final String description)
   {
      this.description = description;
   }

   private @Temporal(TemporalType.DATE)
   Date date;

   public Date getDate()
   {
      return this.date;
   }

   public void setDate(final Date date)
   {
      this.date = date;
   }

   public String toString()
   {
      String result = "";
      if (title != null && !title.trim().isEmpty())
         result += title;
      if (description != null && !description.trim().isEmpty())
         result += " " + description;
      return result;
   }

   @Column
   private ArrayList<Integer> tags = new ArrayList<Integer>();

   public ArrayList<Integer> getTags()
   {
      return this.tags;
   }

   public void setTags(final ArrayList<Integer> tags)
   {
      this.tags = tags;
   }

   @Column
   private int project;

   public int getProject()
   {
      return this.project;
   }

   public void setProject(final int project)
   {
      this.project = project;
   }
}