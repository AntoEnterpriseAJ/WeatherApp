package org.example.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter
public class JsonObjectConverter implements AttributeConverter<JsonObject, Object> {
    @Override
    public Object convertToDatabaseColumn(JsonObject attribute) {
        try {
            PGobject out = new PGobject();
            out.setType("json");
            out.setValue(attribute.toString());
            return out;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Unable to serialize JsonObject", e);
        }
    }

    @Override
    public JsonObject convertToEntityAttribute(Object dbData) {
        if (dbData instanceof PGobject && "json".equals(((PGobject) dbData).getType())) {
            return JsonParser.parseString(((PGobject) dbData).getValue()).getAsJsonObject();
        }
        return null;
    }
}
