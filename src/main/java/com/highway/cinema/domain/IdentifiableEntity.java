package com.highway.cinema.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class IdentifiableEntity {
    private final UUID uuid = UUID.randomUUID();
}
