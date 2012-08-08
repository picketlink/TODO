package org.aerogear.todo.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
@Entity
public class Project implements java.io.Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Version
    @Column(name = "version")
    private int version = 0;

    @Column
    private String title;

    @Column
    private String style;

    @OneToMany
    private Set<Task> tasks = new HashSet<Task>();

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        if (id != null) {
            return id.equals(((Project) that).id);
        }
        return super.equals(that);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(final String style) {
        this.style = style;
    }

    public String toString() {
        String result = "";
        if (title != null && !title.trim().isEmpty())
            result += title;
        if (style != null && !style.trim().isEmpty())
            result += " " + style;
        return result;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }
}