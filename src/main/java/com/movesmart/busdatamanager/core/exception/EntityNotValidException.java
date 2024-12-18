package com.movesmart.busdatamanager.core.exception;

import lombok.Generated;
import lombok.Getter;

@Getter
@Generated
public class EntityNotValidException extends RuntimeException {
    private final String objectType;
    private final String id;

    public EntityNotValidException(String objectType, String id) {
        super("%s with id %s is not valid".formatted(objectType, id));
        this.objectType = objectType;
        this.id = id;
    }
}
