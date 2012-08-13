package org.aerogear.todo.server.serializer;

import org.aerogear.todo.server.model.Tag;
import org.aerogear.todo.server.model.Task;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Replacement to the default serialization strategy for JSON
 */
public class CustomSerializer extends JsonSerializer<Task> {

    @Override
    public void serialize(Task task, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeNumber(task.getId());
        jsonGenerator.writeFieldName("title");
        jsonGenerator.writeString(task.getTitle());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(task.getDescription());
        jsonGenerator.writeFieldName("date");
        jsonGenerator.writeString(task.getDate());
        jsonGenerator.writeFieldName("project");
        jsonGenerator.writeNumber(task.getProject().getId());
        jsonGenerator.writeObjectField("tags", retrieveIds(task.getTags()));
        jsonGenerator.writeEndObject();
    }

    /**
     * This can decrease the server performance
     * @TODO must be replaced by jpa-ql refinement
     *
     */
    private List<Long> retrieveIds(List<Tag> tags){
        List<Long> ids = new ArrayList<Long>();
        for (Tag tag : tags) {
            ids.add(tag.getId());
        }
        return ids;
    }
}
