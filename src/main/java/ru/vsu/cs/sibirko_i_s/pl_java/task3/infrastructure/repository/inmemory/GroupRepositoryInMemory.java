package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupRepositoryInMemory implements GroupRepository {
    private final Map<Integer, Group> storage = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    @Override
    public Optional<Group> findByName(String name) {
        return storage.values()
                .stream()
                .filter(g -> g.getName().equals(name))
                .findFirst();
    }

    @Override
    public Group save(Group object) {
        if (object.getGroupId() == 0) {
            object.setGroupId(idGenerator.getAndIncrement());
            if (object.getCreatedAt() == null) {
                object.setCreatedAt(LocalDateTime.now());
            }
        }

        storage.put(object.getGroupId(), object);
        return object;
    }

    @Override
    public Optional<Group> getById(int id) {
        return Optional.ofNullable(storage.get(id)); //TODO - check how class Optional works
    }

    @Override
    public List<Group> getAll() {
        return storage.values()
                .stream()
                .toList();
    }

    @Override
    public void remove(int id) {
        storage.remove(id);
    }
}
