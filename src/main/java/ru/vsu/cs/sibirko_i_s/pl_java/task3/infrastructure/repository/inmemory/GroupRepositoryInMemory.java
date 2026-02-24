package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

public class GroupRepositoryInMemory implements GroupRepository {
    @Override
    public Optional<Group> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Group save(Group object) {
        return null;
    }

    @Override
    public Optional<Group> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Group> getAll() {
        return List.of();
    }

    @Override
    public void remove(int id) {

    }
}
