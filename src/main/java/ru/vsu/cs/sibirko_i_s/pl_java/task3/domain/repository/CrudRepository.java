package ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <T> {
    //crud - create, read, update, delete
    T save(T object);
    Optional<T> getById(int id);
    List<T> getAll();
    void remove(int id);
}
