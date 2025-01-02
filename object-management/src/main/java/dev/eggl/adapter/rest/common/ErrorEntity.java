package dev.eggl.adapter.rest.common;

public record ErrorEntity(int httpStatus, String errorMessage) {}
