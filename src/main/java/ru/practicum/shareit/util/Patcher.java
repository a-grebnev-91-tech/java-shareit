package ru.practicum.shareit.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Patcher {
    private final ObjectMapper objMapper;

    public Patcher(){
        this.objMapper = new ObjectMapper();
        objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public boolean patch(Object toPatch, Object patch) {
        try {
            objMapper.updateValue(toPatch, patch);
            return true;
        } catch (JsonMappingException e) {
            return false;
        }
    }
}
