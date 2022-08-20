package com.highway.cinema.infrastructure.mocks;

import com.highway.cinema.domain.IdentifiableEntity;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractIdentifyingRepository<T extends IdentifiableEntity> extends AbstractRepository<T> {
    @Override
    public Optional<T> findById(UUID id) {
        return entities.stream()
                .filter(x -> x.getUuid().equals(id))
                .findFirst();
    }

}
