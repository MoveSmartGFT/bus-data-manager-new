package com.movesmart.busdatamanager.core.exception;

import lombok.Generated;
import lombok.Getter;

@Getter
@Generated
public class EntityAlreadyExistsException extends RuntimeException {
    private final String objectType;
    private final String id;

    public EntityAlreadyExistsException(String objectType, String id) {
        super("%s %s already exists".formatted(objectType, id));
        this.objectType = objectType;
        this.id = id;
    }

}
