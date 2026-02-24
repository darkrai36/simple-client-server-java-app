package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.JournalRepository;

import java.util.List;
import java.util.Optional;

public class JournalRepositoryInMemory implements JournalRepository {
    @Override
    public Optional<Journal> findByStudentIdWithStudent(int studentId) {
        return Optional.empty();
    }

    @Override
    public List<Journal> findAllWithStudents() {
        return List.of();
    }

    @Override
    public List<Journal> findByTaskStatus(int number, boolean completed) {
        return List.of();
    }

    @Override
    public List<Journal> findByGroupId(int groupId) {
        return List.of();
    }

    @Override
    public void updateTaskStatus(int studentId, int taskNumber, boolean completed) {

    }

    @Override
    public void markAllTasks(int studentId, boolean completed) {

    }

    @Override
    public Journal save(Journal object) {
        return null;
    }

    @Override
    public Optional<Journal> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Journal> getAll() {
        return List.of();
    }

    @Override
    public void remove(int id) {

    }
}
