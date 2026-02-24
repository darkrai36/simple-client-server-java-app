package ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student> {
    List<Student> findStudentsByGroupId(int groupId);
    Optional<Student> findByFullName(String firstName, String lastName);
    Optional<Student> findByLastName(String lastName);
    void moveStudentToNewGroup(int studentId, int newGroupId);
}
