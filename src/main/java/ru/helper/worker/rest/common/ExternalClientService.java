package ru.helper.worker.rest.common;

public interface ExternalClientService<T, R> {

    R doRequest(T request);
}
