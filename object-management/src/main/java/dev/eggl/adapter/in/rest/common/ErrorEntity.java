package dev.eggl.adapter.in.rest.common;

public record ErrorEntity(int httpStatus, String errorMessage) {
}
