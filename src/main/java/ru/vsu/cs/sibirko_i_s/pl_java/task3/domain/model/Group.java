package ru.vsu.cs.sibirko_i_s.pl_java.task3.domain.model;

import java.time.LocalDateTime;

public class Group {
    private int groupId;
    private String name;
    private LocalDateTime createdAt;

    public Group() {
    }

    public Group(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название группы не введено");
        }
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public Group(int groupId, String name, LocalDateTime createdAt) {
        this.groupId = groupId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя группы не введено!");
        }
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
