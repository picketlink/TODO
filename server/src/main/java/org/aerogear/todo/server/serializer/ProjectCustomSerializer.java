package org.aerogear.todo.server.serializer;

import org.aerogear.todo.server.model.Project;
import org.aerogear.todo.server.model.Tag;
import org.aerogear.todo.server.model.Task;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Replacement to the default serialization strategy for JSON
 */
public class ProjectCustomSerializer extends JsonSerializer<Project> {

    @Override
    public void serialize(Project project, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeNumber(project.getId());
        jsonGenerator.writeFieldName("title");
        jsonGenerator.writeString(project.getTitle());
        jsonGenerator.writeFieldName("style");
        jsonGenerator.writeString(project.getStyle());
        jsonGenerator.writeObjectField("tasks", retrieveIds(project.getTasks()));
        jsonGenerator.writeEndObject();
    }

    /**
     * This can decrease the server performance
     *
     * @TODO must be replaced by jpa-ql refinement
     */
    private List<Long> retrieveIds(Set<Task> tasks) {
        List<Long> ids = new ArrayList<Long>();
        for (Task task : tasks) {
            ids.add(task.getId());
        }
        return ids;
    }
}
