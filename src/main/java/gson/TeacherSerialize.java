package gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dbmodules.entity.Group;
import dbmodules.entity.Teacher;
import dbmodules.types.Gender;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class TeacherSerialize implements JsonSerializer<Teacher> {
    public JsonElement serialize(Teacher teacher, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        object.addProperty("id", teacher.getId());
        object.addProperty("name", teacher.getName());
        object.addProperty("birthday", formatter.format(teacher.getBirth()));
        object.addProperty("gender", teacher.getGender().getValue());

        return object;
    }

}