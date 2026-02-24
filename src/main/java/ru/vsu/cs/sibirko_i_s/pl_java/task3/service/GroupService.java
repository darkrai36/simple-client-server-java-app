package ru.vsu.cs.sibirko_i_s.pl_java.task3.service;

import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model.Group;
import ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.repository.GroupRepository;

public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
}
