package ru.vsu.cs.sibirko_i_s.pl_java.task3.infrastructure.repository.inmemory;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.JournalRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class JournalRepositoryInMemory implements JournalRepository {
    private final Map<Integer, Journal> store = new HashMap<>();

    private final StudentRepository studentRepository;

    public JournalRepositoryInMemory(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Journal save(Journal journal) {
        journal.setUpdatedAt(LocalDateTime.now());
        store.put(journal.getStudentId(), journal);
        return journal;
    }

    @Override
    public Optional<Journal> getById(int studentId) {
        return Optional.ofNullable(store.get(studentId));
    }

    @Override
    public List<Journal> getAll() {
        return store.values()
                .stream()
                .toList();
    }

    @Override
    public void remove(int studentId) {
        store.remove(studentId);
    }

    @Override
    public Optional<Journal> findByStudentIdWithStudent(int studentId) {
        return getById(studentId);
    }

    @Override
    public List<Journal> findAllWithStudents() {
        return getAll();
    }

    @Override
    public List<Journal> findByTaskStatus(int number, boolean completed) {
        return store.values().stream()
                .filter(journal -> {
                    return switch (number) {
                        case 1 -> journal.isTask1Completed() == completed;
                        case 2 -> journal.isTask2Completed() == completed;
                        case 3 -> journal.isTask3Completed() == completed;
                        default -> throw new IllegalArgumentException("Неверный номер задачи: " + number);
                    };
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Journal> findByGroupId(int groupId) {
        List<Student> studentsInGroup = studentRepository.findStudentsByGroupId(groupId);

        Set<Integer> studentIds = studentsInGroup.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toSet());

        return store.values().stream()
                .filter(journal -> studentIds.contains(journal.getStudentId()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTaskStatus(int studentId, int taskNumber, boolean completed) {
        getById(studentId).ifPresent(journal -> {
            switch (taskNumber) {
                case 1 -> journal.setTask1Completed(completed);
                case 2 -> journal.setTask2Completed(completed);
                case 3 -> journal.setTask3Completed(completed);
                default -> throw new IllegalArgumentException("Неверный номер задачи: " + taskNumber);
            }
            journal.setUpdatedAt(LocalDateTime.now());
        });
    }

    @Override
    public void markAllTasks(int studentId, boolean completed) {
        getById(studentId).ifPresent(journal -> {
            journal.setTask1Completed(completed);
            journal.setTask2Completed(completed);
            journal.setTask3Completed(completed);
            journal.setUpdatedAt(LocalDateTime.now());
        });
    }
}