package com.movessmart.busdatamanager.core.exception;

import lombok.Generated;
import lombok.Getter;

@Getter
@Generated
public class EntityStatusException extends RuntimeException {
    private final String objectType;
    private final String id;
    private final String status;

    public EntityStatusException(String objectType, String id, String status) {
        super("%s with id %s is %s".formatted(objectType, id, status));
        this.objectType = objectType;
        this.id = id;
        this.status = status;
    }
}
