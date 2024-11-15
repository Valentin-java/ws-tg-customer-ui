package ru.helper.worker.rest.common;

public interface OrderClientService<T, R> {

    R doRequest(T request);
}
