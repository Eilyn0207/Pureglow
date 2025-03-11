package com.pureGlow.pureGlow.Services.dao;

import jakarta.transaction.Transactional;

import java.util.List;

public interface Idao<T,ID>{

    public T getById(ID id);
    @Transactional
    public  void save(T obje);

    @Transactional
    public void delete(T obje);

    // Para obtener una lista de todas las entidades con paginación
    List<T> findAll();
}
