package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StudentRepositoryInMemory implements StudentRepository {
    private final Map<Integer, Student> storage = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public List<Student> findStudentsByGroupId(int groupId) {
        return storage.values()
                .stream()
                .filter(s -> s.getGroupId() == groupId)
                .toList();
    }

    @Override
    public Optional<Student> findByFullName(String firstName, String lastName) {
        return storage.values()
                .stream()
                .filter(s -> s.getStudentFirstName().equalsIgnoreCase(firstName)
                        && s.getStudentLastName().equalsIgnoreCase(lastName))
                .findFirst(); //TODO read how works method 'findFirst()'
    }

    @Override
    public Optional<Student> findByLastName(String lastName) {
        return storage.values()
                .stream()
                .filter(s -> s.getStudentLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    @Override
    public void moveStudentToNewGroup(int studentId, int newGroupId) {
        getById(studentId).ifPresent(student -> student.setGroupId(newGroupId));
    }

    @Override
    public Student save(Student object) {
        if (object.getStudentId() == 0) {
            object.setStudentId(idGenerator.getAndIncrement());
        }
        storage.put(object.getStudentId(), object);
        return object;
    }

    @Override
    public Optional<Student> getById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Student> getAll() {
        return storage.values()
                .stream()
                .toList();
    }

    @Override
    public void remove(int id) {
        storage.remove(id);
    }
}
