package ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Journal;

import java.util.List;
import java.util.Optional;

public interface JournalRepository extends CrudRepository<Journal>{
    Optional<Journal> findByStudentIdWithStudent(int studentId); // почему WithStudent в названии?
    List<Journal> findAllWithStudents();
    List<Journal> findByTaskStatus(int number, boolean completed); // что за статус таска?
    List<Journal> findByGroupId(int groupId);
    void updateTaskStatus(int studentId, int taskNumber, boolean completed); // зачем передаём логическую переменную?
    void markAllTasks(int studentId, boolean completed);
}
