package dev.eggl.adapter.in.rest;

public record ErrorEntity(int httpStatus, String errorMessage) {
}
