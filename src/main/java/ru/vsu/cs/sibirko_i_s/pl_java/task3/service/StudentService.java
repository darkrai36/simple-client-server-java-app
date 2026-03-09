package ru.vsu.cs.sibirko_i_s.pl_java.task3.service;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.StudentRepository;

import java.util.List;

public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final JournalService journalService;

    public StudentService(StudentRepository studentRepository,
                          GroupRepository groupRepository,
                          JournalService journalService) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.journalService = journalService;
    }

    public Student createStudent(int groupId, String firstName, String lastName) {
        if (groupRepository.getById(groupId).isEmpty()) {
            throw new IllegalArgumentException("Группа с ID " + groupId + " не найдена!");
        }

        Student student = new Student(firstName, lastName);
        student.setGroupId(groupId);

        Student savedStudent = studentRepository.save(student);

        journalService.createJournalForStudent(savedStudent.getStudentId());

        return savedStudent;
    }

    public void deleteStudent(int studentId) {
        journalService.deleteJournal(studentId);
        studentRepository.remove(studentId);
    }

    public Student getStudentById(int studentId) {
        return studentRepository.getById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент с ID " + studentId + " не найден."));
    }

    public List<Student> getStudentsByGroupId(int groupId) {
        return studentRepository.findStudentsByGroupId(groupId);
    }

    public void moveStudentToNewGroup(int studentId, int newGroupId) {
        if (groupRepository.getById(newGroupId).isEmpty()) {
            throw new IllegalArgumentException("Целевая группа с ID " + newGroupId + " не найдена!");
        }
        studentRepository.moveStudentToNewGroup(studentId, newGroupId);
    }
}
