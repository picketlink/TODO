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
package org.aerogear.todo.server.model;

import org.aerogear.todo.server.serializer.TaskCustomSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.aerogear.todo.server.util.DateBuilder.newDateBuilder;

@XmlRootElement
@JsonSerialize(using = TaskCustomSerializer.class)
@Entity
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    @Column
    private String title;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private Calendar date;

    @OneToMany
    private List<Tag> tags = new ArrayList<Tag>();

    @OneToOne
    private Project project;

    public Task() {
    }

    public Task(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
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
            return id.equals(((Task) that).id);
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDate() {
        return newDateBuilder().withCalendar(date).build();
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public String toString() {
        String result = "";
        if (title != null && !title.trim().isEmpty())
            result += title;
        if (description != null && !description.trim().isEmpty())
            result += " " + description;
        return result;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(final List<Tag> tags) {
        this.tags = tags;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }
}