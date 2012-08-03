package org.aerogear.todo.server.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import java.util.Set;
import java.util.HashSet;
import org.aerogear.todo.server.model.Task;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Project implements java.io.Serializable
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
         return id.equals(((Project) that).id);
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

   @Column
   private String style;

   public String getStyle()
   {
      return this.style;
   }

   public void setStyle(final String style)
   {
      this.style = style;
   }

   public String toString()
   {
      String result = "";
      if (title != null && !title.trim().isEmpty())
         result += title;
      if (style != null && !style.trim().isEmpty())
         result += " " + style;
      return result;
   }

   @OneToMany
   private Set<Task> tasks = new HashSet<Task>();

   public Set<Task> getTasks()
   {
      return this.tasks;
   }

   public void setTasks(final Set<Task> tasks)
   {
      this.tasks = tasks;
   }
}