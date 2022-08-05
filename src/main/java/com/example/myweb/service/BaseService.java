package com.example.myweb.service;

import java.util.List;
import java.util.Optional;

public interface BaseService <T>{
    T save (T t);

    List<T> findAll();

    Optional<T> findById(Long id);

    void remove(Long id);
}
