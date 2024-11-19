package ru.helper.worker.rest.external.common;

public interface ExternalClientService<T, R> {

    R doRequest(T request);
}
