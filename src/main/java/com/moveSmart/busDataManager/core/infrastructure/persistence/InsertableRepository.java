package com.moveSmart.busDataManager.core.infrastructure.persistence;

public interface InsertableRepository<T> {
    <S extends T> S insert(S entity);
}
