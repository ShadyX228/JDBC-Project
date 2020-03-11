package gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dbmodules.entity.Student;
import dbmodules.entity.Teacher;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class StudentSerialize implements JsonSerializer<Student> {
    public JsonElement serialize(Student student, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        object.addProperty("id", student.getId());
        object.addProperty("name", student.getName());
        object.addProperty("birthday", formatter.format(student.getBirth()));
        object.addProperty("gender", student.getGender().getValue());
        object.addProperty("group", student.getGroup().getNumber());

        return object;
    }
}