package ru.vsu.cs.sibirko_i_s.pl_java.task3.service;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Student;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;

import java.util.List;

public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentService studentService;

    public GroupService(GroupRepository groupRepository, StudentService studentService) {
        this.groupRepository = groupRepository;
        this.studentService = studentService;
    }

    public Group createGroup(String name) {
        if (groupRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Группа с названием '" + name + "' уже существует!");
        }

        Group group = new Group(name);
        return groupRepository.save(group);
    }

    public void deleteGroup(int groupId) {
        Group group = groupRepository.getById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Группа с ID " + groupId + " не найдена."));

        List<Student> students = studentService.getStudentsByGroupId(groupId);
        for (Student student : students) {
            studentService.deleteStudent(student.getStudentId());
        }

        groupRepository.remove(groupId);
    }

    public Group getGroupById(int groupId) {
        return groupRepository.getById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Группа с ID " + groupId + " не найдена."));
    }

    public List<Group> getAllGroups() {
        return groupRepository.getAll();
    }
}