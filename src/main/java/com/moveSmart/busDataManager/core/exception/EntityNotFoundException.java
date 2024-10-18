package com.moveSmart.busDataManager.core.exception;

import lombok.Generated;
import lombok.Getter;

@Getter
@Generated
public class EntityNotFoundException extends RuntimeException {
    private final String objectType;
    private final String id;

    public EntityNotFoundException(String objectType, String id) {
        super("%s with id %s does not exist".formatted(objectType, id));
        this.objectType = objectType;
        this.id = id;
    }
}
