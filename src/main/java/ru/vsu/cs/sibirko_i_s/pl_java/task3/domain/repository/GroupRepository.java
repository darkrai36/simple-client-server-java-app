package ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group> {
    Optional<Group> findByName(String name);
}
