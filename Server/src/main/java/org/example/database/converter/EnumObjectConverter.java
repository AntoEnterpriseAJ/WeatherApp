package org.example.database.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.database.model.enums.Role;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = true)
public class EnumObjectConverter implements AttributeConverter<Role, Object> {
    public Object convertToDatabaseColumn(Role role)
    {
        try {
            PGobject out = new PGobject();
            out.setType("ROLE");
            out.setValue(role.toString());
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to serialize Role enum");
        }
    }

    @Override
    public Role convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }

        if (dbData instanceof String stringValue) {
            return Role.valueOf(stringValue.trim());
        }

        throw new IllegalArgumentException("Invalid database data for Role conversion: " + dbData);
    }
}
