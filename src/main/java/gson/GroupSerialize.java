package gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dbmodules.entity.Group;

import java.lang.reflect.Type;

public class GroupSerialize implements JsonSerializer<Group> {
    @Override
    public JsonElement serialize(Group group, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", group.getId());
        object.addProperty("number", group.getNumber());
        return object;
    }
}