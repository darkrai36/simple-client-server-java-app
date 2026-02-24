package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

public class StudentRepositoryInMemory implements StudentRepository {
    @Override
    public List<Student> findStudentsByGroupId(int groupId) {
        return List.of();
    }

    @Override
    public Optional<Student> findByFullName(String firstName, String lastName) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> findByLastName(String lastName) {
        return Optional.empty();
    }

    @Override
    public void moveStudentToNewGroup(int studentId, int newGroupId) {

    }

    @Override
    public Student save(Student object) {
        return null;
    }

    @Override
    public Optional<Student> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        return List.of();
    }

    @Override
    public void remove(int id) {

    }
}
