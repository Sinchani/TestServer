package net.media.spamserver.model;

import net.media.spamserver.exception.StoredProcedureCreationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by vivek on 5/27/15.
 */
public class StoredProcedure {
    private Map<String, Object> stringParameters = new HashMap<String, Object>();
    private Map<String, Object> integerParameters = new HashMap<String, Object>();
    private String name;

    public StoredProcedure setName(String name) {
        this.name = name;
        return this;
    }

    public StoredProcedure addStringParameter(String key, Object value) {
        stringParameters.put(key, value);
        return this;
    }

    public StoredProcedure addIntegerParameter(String key, Object value) {
        integerParameters.put(key, value);
        return this;
    }

    public String buildStoredProcedure() throws StoredProcedureCreationException {
        if (name == null || name.equals("")) {
            throw new StoredProcedureCreationException("Name of the procedure is invalid");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("exec ").append(name);
        for (Map.Entry<String, Object> entry : stringParameters.entrySet()) {
            String value = entry.getValue() == null ? "null" : entry.getValue().toString();
            stringBuilder.append(" @").append(entry.getKey()).append("=\'").append(value).append("\', ");
        }
        for (Map.Entry<String, Object> entry : integerParameters.entrySet()) {
            String value = entry.getValue() == null ? "null" : entry.getValue().toString();
            stringBuilder.append(" @").append(entry.getKey()).append("=").append(value).append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
