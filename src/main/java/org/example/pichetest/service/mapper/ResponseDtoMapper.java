package org.example.pichetest.service.mapper;

public interface ResponseDtoMapper<D, T> {
    D mapToDto(T t);
}
